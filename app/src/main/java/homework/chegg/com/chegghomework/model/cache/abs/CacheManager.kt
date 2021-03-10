package homework.chegg.com.chegghomework.model.cache.abs

import homework.chegg.com.chegghomework.model.DataModel

/**
 * This class provides a caching mechanism for data models retrieved from data sources.
 * It allows implementation of different caching methods (database, shared preferences, external storage etc.),
 * as well as a stale time, after which the data model will be deleted from the current cache,
 * according to the respective cache method.
 *
 * With the help of 'standbyList', it can prepare all the data models for later caching.
 * Whenever you call 'saveAll()' method, it saves all the prepared data models from 'standbyList'
 * to the current cache method. This allows the caller to separate the preparing the data models
 * and saving them in cache.
 */
abstract class CacheManager {
    /**
     * The stale time in minutes after which the data model will be deleted from the cache.
     */
    abstract val staleTimeMinutes: Int

    /**
     * This represents the current caching method.
     * This is used for performing caching operations (read, save and delete) on the correct
     * cache method, e.g., suppose you have a class "A" that inherits this class and it implements
     * a database caching. At some point it caches the data in a database, this means it has to read
     * the same data from the database as well. It must preserve consistency of the operations on
     * the current caching method.
     */
    abstract val staleType: String

    /**
     * A list provided to prepare all the data models the caller wish to cache later on.
     */
    private val standbyList: MutableList<DataModel> = mutableListOf()

    fun hasElapsedStaleTime(dataModel: DataModel) : Boolean {
        val dataModelLifeTimeMillis = System.currentTimeMillis() - dataModel.createdAtTimeMillis
        return dataModelLifeTimeMillis > (staleTimeMinutes * 60 * 1000) // converting stale time in minutes to milliseconds
    }

    fun addToStandbyList(dataModels: List<DataModel>) {
        standbyList.addAll(dataModels)
    }

    fun clearStandbyList() {
        standbyList.clear()
    }

    suspend fun saveAll() {
        standbyList.forEach {
            it.createdAtTimeMillis = System.currentTimeMillis()
            it.staleType = staleType
            saveData(it)
        }

        standbyList.clear()
    }

    abstract suspend fun saveData(dataModel: DataModel)
    abstract suspend fun readData() : List<DataModel>
    abstract suspend fun deleteData(dataModels: List<DataModel>)
    abstract suspend fun deleteData(dataModel: DataModel)
}