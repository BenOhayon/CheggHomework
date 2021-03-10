package homework.chegg.com.chegghomework.model.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import homework.chegg.com.chegghomework.model.DataModel

/**
 * This class represents a database singleton instance of the local database on the device.
 */
@Database(entities = [DataModel::class], version = 3, exportSchema = false)
abstract class DataModelDatabase : RoomDatabase() {

    abstract fun dataModelDao() : DataModelDao

    companion object {
        @Volatile
        private var instance : DataModelDatabase? = null

        fun getDatabase(context: Context) : DataModelDatabase {
            val tempInstance = instance
            if (tempInstance != null)
                return tempInstance

            synchronized(this) {
                val newDatabase = Room.databaseBuilder(
                        context.applicationContext,
                        DataModelDatabase::class.java,
                        "data_model_database"
                ).fallbackToDestructiveMigration()
                        .build()

                instance = newDatabase
                return instance!!
            }
        }
    }
}