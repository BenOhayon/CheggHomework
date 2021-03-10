package homework.chegg.com.chegghomework.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import homework.chegg.com.chegghomework.model.DataModel
import homework.chegg.com.chegghomework.model.ModelRepository
import homework.chegg.com.chegghomework.model.SourceProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivityViewModel : ViewModel() {

    private val modelRepository: ModelRepository = ModelRepository()
    var updateUI = true

    fun getReceivedLiveData() : LiveData<List<DataModel>> = modelRepository.receivedLiveData
    fun getCachedLiveData() : LiveData<List<DataModel>> = modelRepository.cachedLiveData

    fun getData(sourceProviders: Map<String, SourceProvider>) {
        viewModelScope.launch(Dispatchers.IO) {
            modelRepository.getData(sourceProviders)
        }
    }

    fun setSourceProviders(sourceProviders: Map<String, SourceProvider>) {
        modelRepository.setSourceProviders(sourceProviders)
    }

    fun deleteData(dataModel: DataModel) {
        modelRepository.deleteData(dataModel)
    }
}