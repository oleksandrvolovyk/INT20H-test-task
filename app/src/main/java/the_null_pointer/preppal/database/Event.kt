package the_null_pointer.preppal.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.Date

@Entity
data class Event(
    @PrimaryKey(autoGenerate = true)
    val id: Long,

    @ColumnInfo(name = "summary")
    val summary: String?,

    @ColumnInfo(name = "type")
    val type: String?,

    @ColumnInfo(name = "location")
    val location: String?,

    @ColumnInfo(name = "description")
    val description: String?,

    @ColumnInfo(name = "start")
    @TypeConverters(DateConverter::class)
    val start: Date?,

    @ColumnInfo(name = "end")
    @TypeConverters(DateConverter::class)
    val end: Date?,

    @ColumnInfo(name = "recurrence")
    val recurrence: String?,

    @ColumnInfo(name = "reminder")
    val reminder: String?,

    @ColumnInfo(name = "score")
    val score: Double?,

    @ColumnInfo(name = "max_score")
    val maxScore: Double?
)