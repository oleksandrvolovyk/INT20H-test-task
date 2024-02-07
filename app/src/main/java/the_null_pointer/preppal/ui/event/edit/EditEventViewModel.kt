package the_null_pointer.preppal.ui.event.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import the_null_pointer.preppal.R
import the_null_pointer.preppal.data.Event
import the_null_pointer.preppal.data.EventRepository
import the_null_pointer.preppal.data.TimestampMillis
import the_null_pointer.preppal.ui.SideEffect
import the_null_pointer.preppal.ui.event.BaseEventScreenUiState
import the_null_pointer.preppal.util.TimeUtil.MILLISECONDS_IN_DAY
import javax.inject.Inject

data class EditEventScreenUiState(
    override val summary: String = "",
    override val type: Event.Type = Event.Type.Task,
    override val recurrenceType: Event.RecurrenceType? = null,
    override val recurrenceEndDate: TimestampMillis = System.currentTimeMillis(),
    override val start: TimestampMillis = System.currentTimeMillis(),
    override val end: TimestampMillis = System.currentTimeMillis(),
    override val isReminderEnabled: Boolean = false,
    override val reminderOffsets: List<TimestampMillis> = emptyList(),

    override val isLocationEnabled: Boolean = false,
    override val locationLatitude: Double? = null,
    override val locationLongitude: Double? = null,

    override val isGraded: Boolean = false
) : BaseEventScreenUiState

enum class EventDeleteMethod {
    DeleteOne, DeleteOneAndFollowing, DeleteAll
}

@HiltViewModel
class EditEventViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val eventRepository: EventRepository
) : ViewModel() {

    private val eventId: Long = savedStateHandle["eventId"]!!

    private val _uiState = MutableStateFlow(EditEventScreenUiState())
    val uiState: StateFlow<EditEventScreenUiState> = _uiState.asStateFlow()

    private val _sideEffectChannel = Channel<SideEffect>(capacity = Channel.BUFFERED)
    val sideEffectFlow: Flow<SideEffect>
        get() = _sideEffectChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            val event = eventRepository.getById(eventId)

            val allEventsOfThisType = event?.let {
                eventRepository.getAllBySummaryAndType(event.summary, event.type)
            }

            val lastRecurringEventDateMillis =
                if (event?.recurrence != null && !allEventsOfThisType.isNullOrEmpty()) {
                    val lastRecurringEventTimeMillis = allEventsOfThisType.maxOf { it.start }

                    lastRecurringEventTimeMillis - lastRecurringEventTimeMillis % MILLISECONDS_IN_DAY
                } else {
                    null
                }

            if (event != null) {
                _uiState.update {
                    it.copy(
                        summary = event.summary,
                        type = event.type,
                        recurrenceType = event.recurrence,
                        recurrenceEndDate = lastRecurringEventDateMillis ?: event.start,
                        start = event.start,
                        end = event.end,
                        isReminderEnabled = event.reminder.isNotEmpty(),
                        reminderOffsets = event.reminder.map { reminderTimeMillis -> reminderTimeMillis - event.start },
                        isLocationEnabled = event.location != null,
                        locationLatitude = event.location?.latitude,
                        locationLongitude = event.location?.longitude,
                        isGraded = event.graded
                    )
                }
            }
        }
    }

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

    fun updateRecurrenceEndDate(newEndDateMillis: Long) {
        _uiState.update {
            it.copy(
                recurrenceEndDate = newEndDateMillis
            )
        }
    }

    fun updateStartDate(newStartDateMillis: Long) {
        if (newStartDateMillis <= uiState.value.end) {
            _uiState.update {
                it.copy(
                    start = newStartDateMillis
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    start = newStartDateMillis,
                    end = newStartDateMillis
                )
            }
        }

        // If new start date is larger than current recurrence end date, move the recurrence end to
        // make sure that startDate is always <= recurrenceEndDate
        if (newStartDateMillis > uiState.value.recurrenceEndDate) {
            _uiState.update {
                it.copy(
                    recurrenceEndDate = newStartDateMillis
                )
            }
        }
    }

    fun updateEndDate(newEndDateMillis: Long) {
        if (newEndDateMillis >= uiState.value.start) {
            _uiState.update {
                it.copy(
                    end = newEndDateMillis
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    start = newEndDateMillis,
                    end = newEndDateMillis
                )
            }
        }
    }

    fun updateReminderState(newReminderState: Boolean) {
        _uiState.update {
            it.copy(
                isReminderEnabled = newReminderState
            )
        }
    }

    fun updateReminders(newReminderOffsets: List<TimestampMillis>) {
        _uiState.update {
            it.copy(
                reminderOffsets = newReminderOffsets.sortedDescending()
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
        TODO()
    }

    fun deleteEvent(eventDeleteMethod: EventDeleteMethod) {
        _sideEffectChannel.trySend(
            SideEffect.ConfirmAction(
                R.string.confirm_event_deletion
            ) {
                viewModelScope.launch {
                    when (eventDeleteMethod) {
                        EventDeleteMethod.DeleteOne -> {
                            eventRepository.delete(eventId)
                            _sideEffectChannel.trySend(SideEffect.NavigateBack)
                        }

                        EventDeleteMethod.DeleteOneAndFollowing -> {
                            val eventIdsToBeDeleted = eventRepository.getAllBySummaryAndType(
                                uiState.value.summary,
                                uiState.value.type
                            ).filter { it.start >= uiState.value.start }
                                .map { it.id }

                            eventRepository.delete(eventIdsToBeDeleted)
                            _sideEffectChannel.trySend(SideEffect.NavigateBack)
                        }

                        EventDeleteMethod.DeleteAll -> {
                            val eventIdsToBeDeleted = eventRepository.getAllBySummaryAndType(
                                uiState.value.summary,
                                uiState.value.type
                            ).map { it.id }

                            eventRepository.delete(eventIdsToBeDeleted)
                            _sideEffectChannel.trySend(SideEffect.NavigateBack)
                        }
                    }
                }
            }
        )
    }
}