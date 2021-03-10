package homework.chegg.com.chegghomework.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * This is a data class which represents the model given by the difference data sources.
 */
@Entity(tableName = "data_models")
data class DataModel(
        @PrimaryKey(autoGenerate = true)
        val id: Int,
        var title: String = "",
        var subtitle: String = "",
        var imageUrl: String = "",
        var createdAtTimeMillis: Long = 0L,
        var staleType: String = ""
)