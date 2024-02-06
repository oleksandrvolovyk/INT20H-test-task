package the_null_pointer.preppal.data

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.time.LocalDateTime

@Entity
data class Event(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "summary")
    val summary: String,

    @ColumnInfo(name = "type")
    val type: Type,

    @Embedded
    val location: Location? = null,

    @ColumnInfo(name = "start")
    val start: TimestampMillis,

    @ColumnInfo(name = "end")
    val end: TimestampMillis,

    @ColumnInfo(name = "recurrence")
    val recurrence: RecurrenceType? = null,

    @ColumnInfo(name = "reminder")
    @TypeConverters(TimestampListConverter::class)
    val reminder: List<TimestampMillis> = emptyList(),

    @ColumnInfo(name = "graded")
    val graded: Boolean = false,

    @ColumnInfo(name = "grade")
    val grade: Double? = null,

    @ColumnInfo(name = "max_score")
    val maxGrade: Double? = null
) {
    enum class Type {
        Lecture, Exam, Lab, Practice, Task, Seminar, Workshop
    }

    enum class RecurrenceType {
        Daily, EveryWorkDay, EveryWeekend, Weekly, Monthly
    }
}

typealias TimestampMillis = Long

data class Location(
    val latitude: Double,
    val longitude: Double
)