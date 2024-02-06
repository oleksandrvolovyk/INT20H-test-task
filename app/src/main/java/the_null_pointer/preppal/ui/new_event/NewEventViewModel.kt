package the_null_pointer.preppal.ui.new_event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import the_null_pointer.preppal.data.Event
import the_null_pointer.preppal.data.EventRepository
import the_null_pointer.preppal.data.TimestampMillis
import javax.inject.Inject

data class NewEventScreenUiState(
    val summary: String = "",
    val type: Event.Type = Event.Type.Task,
    val start: TimestampMillis = System.currentTimeMillis(),
    val end: TimestampMillis = System.currentTimeMillis()
)

// TODO: Finish "New Event" screen

@HiltViewModel
class NewEventViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NewEventScreenUiState())
    val uiState: StateFlow<NewEventScreenUiState> = _uiState.asStateFlow()

    fun updateSummary(newSummary: String) {
        _uiState.update {
            it.copy(summary = newSummary)
        }
    }

    fun updateEventType(newEventType: Event.Type) {
        _uiState.update {
            it.copy(type = newEventType)
        }
    }

    fun updateStartDate(newStartDateMillis: Long) {
        _uiState.update {
            it.copy(
                start = newStartDateMillis
            )
        }
    }

    fun updateEndDate(newEndDateMillis: Long) {
        _uiState.update {
            it.copy(
                end = newEndDateMillis
            )
        }
    }

    fun submitEvent() = viewModelScope.launch {
        eventRepository.insert(
            Event(
                summary = uiState.value.summary,
                type = uiState.value.type,
                start = uiState.value.start,
                end = uiState.value.end
            )
        )
    }
}