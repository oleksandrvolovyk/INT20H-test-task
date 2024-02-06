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
import the_null_pointer.preppal.data.Location
import the_null_pointer.preppal.data.TimestampMillis
import javax.inject.Inject

data class NewEventScreenUiState(
    val summary: String = "",
    val type: Event.Type = Event.Type.Task,
    val recurrenceType: Event.RecurrenceType? = null,
    val start: TimestampMillis = System.currentTimeMillis(),
    val end: TimestampMillis = System.currentTimeMillis(),
    val isReminderEnabled: Boolean = false,

    val isLocationEnabled: Boolean = false,
    val locationLatitude: Double? = null,
    val locationLongitude: Double? = null,

    val isGraded: Boolean = false
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

    fun updateRecurrenceType(newRecurrenceType: Event.RecurrenceType?) {
        _uiState.update {
            it.copy(recurrenceType = newRecurrenceType)
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

    fun updateReminderState(newReminderState: Boolean) {
        _uiState.update {
            it.copy(
                isReminderEnabled = newReminderState
            )
        }
    }

    fun updateLocationState(newLocationState: Boolean) {
        _uiState.update {
            it.copy(
                isLocationEnabled = newLocationState
            )
        }
    }

    fun updateLocation(latitude: Double, longitude: Double) {
        _uiState.update {
            it.copy(
                locationLatitude = latitude,
                locationLongitude = longitude
            )
        }
    }

    fun updateGradedState(newGradedState: Boolean) {
        _uiState.update {
            it.copy(
                isGraded = newGradedState
            )
        }
    }

    fun submitEvent() = viewModelScope.launch {
        val latitude = uiState.value.locationLatitude
        val longitude = uiState.value.locationLongitude

        val location =
            if (latitude != null && longitude != null) Location(latitude, longitude) else null

        eventRepository.insert(
            Event(
                summary = uiState.value.summary,
                type = uiState.value.type,
                location = location,
                start = uiState.value.start,
                end = uiState.value.end,
                recurrence = uiState.value.recurrenceType,
                reminder = emptyList(), // TODO
                graded = uiState.value.isGraded
            )
        )
    }
}