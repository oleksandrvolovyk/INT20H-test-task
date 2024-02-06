package the_null_pointer.preppal.ui.calendar

import the_null_pointer.preppal.database.Event
import java.time.LocalDateTime
import java.time.YearMonth

data class UiEvent(
    val id: Long,
    val summary: String,
    val type: Event.EventType,
    val location: String?,
    val description: String?,
    val start: LocalDateTime,
    val end: LocalDateTime,
    val recurrence: Event.RecurrenceType?,
    val reminder: Long?,
    val score: Double?,
    val maxScore: Double?
)

fun generateEvents(): List<UiEvent> = buildList {
    val currentMonth = YearMonth.now()

    currentMonth.atDay(17).also { date ->
        add(
            UiEvent(
                id = 0,
                summary = "Комп'ютерні системи",
                type = Event.EventType.Lecture,
                location = null,
                description = null,
                start = date.atTime(8, 30),
                end = date.atTime(10, 5),
                recurrence = null,
                reminder = null,
                score = null,
                maxScore = null
            )
        )
        add(
            UiEvent(
                id = 0,
                summary = "Англійська",
                type = Event.EventType.Practice,
                location = null,
                description = null,
                start = date.atTime(10, 25),
                end = date.atTime(12, 20),
                recurrence = null,
                reminder = null,
                score = null,
                maxScore = null
            )
        )
    }
}
