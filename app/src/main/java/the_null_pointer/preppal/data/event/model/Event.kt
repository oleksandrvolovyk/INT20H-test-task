package the_null_pointer.preppal.data.event.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import the_null_pointer.preppal.R
import the_null_pointer.preppal.data.event.TimestampListConverter

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
    val maxGrade: Double? = null,

    /**
     * Means "completed" or "attended", depending on Event Type
     */
    @ColumnInfo(name = "completed")
    val completed: Boolean? = null
) {
    enum class Type {
        Lecture, Exam, Lab, Practice, Task, Seminar;

        companion object {
            val Type.stringResourceId: Int
                get() = when (this) {
                    Lecture -> R.string.lecture
                    Exam -> R.string.exam
                    Lab -> R.string.lab
                    Practice -> R.string.practice
                    Task -> R.string.task
                    Seminar -> R.string.seminar
                }

            val CompletableTypes = listOf(Lecture, Exam, Practice, Task, Seminar)

            val Type.completionStringResourceId: Int
                get() = when (this) {
                    Lecture, Practice, Seminar -> R.string.attended
                    Exam -> R.string.passed
                    Task -> R.string.done
                    else -> R.string.done
                }
        }
    }

    enum class RecurrenceType {
        Daily, EveryWorkDay, EveryWeekend, Weekly, Monthly;

        companion object {
            val RecurrenceType.stringResourceId: Int
                get() = when (this) {
                    Daily -> R.string.daily
                    EveryWorkDay -> R.string.every_work_day
                    EveryWeekend -> R.string.every_weekend
                    Weekly -> R.string.weekly
                    Monthly -> R.string.monthly
                }
        }
    }

    companion object {
        val Empty = Event(
            summary = "",
            type = Type.Task,
            start = 0,
            end = 0
        )
    }
}

typealias TimestampMillis = Long

data class Location(
    val latitude: Double,
    val longitude: Double
)