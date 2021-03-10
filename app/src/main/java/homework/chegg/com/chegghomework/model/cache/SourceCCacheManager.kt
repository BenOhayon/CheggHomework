package homework.chegg.com.chegghomework.model.cache

import androidx.lifecycle.LiveData
import homework.chegg.com.chegghomework.CheggHomeAssignmentApplication
import homework.chegg.com.chegghomework.model.DataModel
import homework.chegg.com.chegghomework.model.cache.abs.CacheManager
import homework.chegg.com.chegghomework.model.db.DataModelDao
import homework.chegg.com.chegghomework.model.db.DataModelDatabase

class SourceCCacheManager : CacheManager() {

    private val dataModelDao: DataModelDao = DataModelDatabase.getDatabase(CheggHomeAssignmentApplication.context).dataModelDao()
    private val cachedDataModels: LiveData<List<DataModel>>

    init {
        cachedDataModels = dataModelDao.getAllDataModels()
    }

    override val staleTimeMinutes: Int
        get() = 60

    override val staleType: String
        get() = "C_STALE_TYPE"

    override suspend fun saveData(dataModel: DataModel) = dataModelDao.saveDataModel(dataModel)
    override suspend fun readData(): List<DataModel> = dataModelDao.getAllDataModels().value!!
    override suspend fun deleteData(dataModel: DataModel) = dataModelDao.deleteDataModel(dataModel)
    override suspend fun deleteData(dataModels: List<DataModel>) = dataModelDao.deleteDataModels(dataModels)
}