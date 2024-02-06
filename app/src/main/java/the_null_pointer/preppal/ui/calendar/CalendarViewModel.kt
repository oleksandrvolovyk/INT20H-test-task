package the_null_pointer.preppal.ui.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import the_null_pointer.preppal.data.Event
import the_null_pointer.preppal.data.EventRepository
import javax.inject.Inject

data class CalendarScreenUiState(
    val events: List<Event> = emptyList()
)

@HiltViewModel
class CalendarViewModel @Inject constructor(private val eventRepository: EventRepository): ViewModel() {
    val uiState: StateFlow<CalendarScreenUiState> = eventRepository.observeEvents()
        .map { CalendarScreenUiState(it) }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            CalendarScreenUiState()
        )
}