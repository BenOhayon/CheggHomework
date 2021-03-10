package homework.chegg.com.chegghomework.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import homework.chegg.com.chegghomework.CheggHomeAssignmentApplication
import homework.chegg.com.chegghomework.model.concurrent.TaskDispatcher
import homework.chegg.com.chegghomework.model.db.DataModelDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.launch

/**
 * This is an intermediate class that delegates all the operations that can be made on the model
 * of the application.
 */
class ModelRepository {

    val receivedLiveData: MutableLiveData<List<DataModel>> = MutableLiveData()
    val cachedLiveData: LiveData<List<DataModel>> = DataModelDatabase.getDatabase(CheggHomeAssignmentApplication.context).dataModelDao().getAllDataModels()
    private val taskDispatcher: TaskDispatcher = TaskDispatcher()
    private var sourceProvidersMap: Map<String, SourceProvider> = mapOf()

    /**
     * This is the main function for retrieving the data from the data sources.
     * It accepts a map that maps the stale type to its source provider.
     */
    fun getData(sourceProvidersMap: Map<String, SourceProvider>) {
        // send requests asynchronously for all sources
        this.sourceProvidersMap = sourceProvidersMap
        val sourceProviders = sourceProvidersMap.values
        val asyncTasks = mutableListOf<Runnable>()
        for (sourceProvider in sourceProviders) {
            asyncTasks.add(Runnable {
                sourceProvider.requestBuilder.buildRequest(onSuccess = { dataModels ->
                    sourceProvider.cacheManager.addToStandbyList(dataModels)
                    taskDispatcher.aggregateResult(dataModels)
                }, onFail = {
                    taskDispatcher.cancelAll()
                    receivedLiveData.value = emptyList()
                })
            })
        }

        taskDispatcher.setOnCancel {
            sourceProviders.forEach {
                it.cacheManager.clearStandbyList()
            }
        }

        taskDispatcher.executeAll(asyncTasks) {
            CoroutineScope(Dispatchers.IO).launch {
                sourceProviders.forEach {
                    it.cacheManager.saveAll()
                }
            }
        }
    }

    fun setSourceProviders(sourceProviders: Map<String, SourceProvider>) {
        this.sourceProvidersMap = sourceProviders
    }

    fun deleteData(dataModel: DataModel) {
        CoroutineScope(Dispatchers.IO).launch {
            sourceProvidersMap[dataModel.staleType]?.cacheManager?.deleteData(dataModel)
        }
    }
}