package homework.chegg.com.chegghomework.model.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import homework.chegg.com.chegghomework.model.DataModel

/**
 * This interface delegates operations on a local database.
 */
@Dao
interface DataModelDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveDataModel(dataModel: DataModel)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveDataModels(dataModels: List<DataModel>)

    @Delete
    suspend fun deleteDataModel(dataModel: DataModel)

    @Delete
    suspend fun deleteDataModels(dataModels: List<DataModel>)

    @Query("select * from data_models order by id asc")
    fun getAllDataModels() : LiveData<List<DataModel>>
}