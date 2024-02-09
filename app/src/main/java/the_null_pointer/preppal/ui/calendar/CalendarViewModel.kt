package the_null_pointer.preppal.ui.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import the_null_pointer.preppal.data.Event
import the_null_pointer.preppal.data.EventRepository
import the_null_pointer.preppal.util.TimeUtil.toEpochDay
import java.time.LocalDate
import javax.inject.Inject

data class CalendarScreenUiState(
    val events: Map<LocalDate, List<Event>> = emptyMap()
)

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {
    val uiState: StateFlow<CalendarScreenUiState> = eventRepository.observeEvents()
        .map { events ->
            CalendarScreenUiState(
                events
                    .sortedBy { it.start }
                    .groupBy { LocalDate.ofEpochDay(it.start.toEpochDay()) }
            )
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            CalendarScreenUiState()
        )

    fun updateEventCompletion(eventId: Long, completed: Boolean) = viewModelScope.launch {
        eventRepository.setEventCompletion(eventId, completed)
    }
}