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
import the_null_pointer.preppal.data.Location
import the_null_pointer.preppal.data.TimestampMillis
import the_null_pointer.preppal.ui.SideEffect
import the_null_pointer.preppal.ui.event.BaseEventScreenUiState
import the_null_pointer.preppal.util.TimeUtil.MILLISECONDS_IN_DAY
import the_null_pointer.preppal.util.TimeUtil.toEpochDay
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

    override val isGraded: Boolean = false,

    val availableEditMethods: List<EventEditMethod> = listOf(EventEditMethod.EditOne)
) : BaseEventScreenUiState

enum class EventDeleteMethod {
    DeleteOne, DeleteOneAndFollowing, DeleteAll
}

enum class EventEditMethod {
    EditOne, EditAll
}

@HiltViewModel
class EditEventViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val eventRepository: EventRepository
) : ViewModel() {

    private val eventId: Long = savedStateHandle["eventId"]!!
    private var originalEvent: Event? = null

    private val _uiState = MutableStateFlow(EditEventScreenUiState())
    val uiState: StateFlow<EditEventScreenUiState> = _uiState.asStateFlow()

    private val _sideEffectChannel = Channel<SideEffect>(capacity = Channel.BUFFERED)
    val sideEffectFlow: Flow<SideEffect>
        get() = _sideEffectChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            val event = eventRepository.getById(eventId)
            originalEvent = event

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
                        isGraded = event.graded,
                        availableEditMethods = if ((allEventsOfThisType?.size ?: 1) > 1) {
                            listOf(EventEditMethod.EditOne, EventEditMethod.EditAll)
                        } else {
                            listOf(EventEditMethod.EditOne)
                        }
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

    fun updateStartDate(newStartDateMillis: Long) {
        // Prevent the user from editing all events if the user has changed the day of the Event
        if (uiState.value.start.toEpochDay() != newStartDateMillis.toEpochDay()) {
            _uiState.update {
                it.copy(
                    availableEditMethods = listOf(EventEditMethod.EditOne)
                )
            }
        }

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
    }

    fun updateEndDate(newEndDateMillis: Long) {
        // Prevent the user from editing all events if the user has changed the day of the Event
        if (uiState.value.start.toEpochDay() != newEndDateMillis.toEpochDay()) {
            _uiState.update {
                it.copy(
                    availableEditMethods = listOf(EventEditMethod.EditOne)
                )
            }
        }

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

    fun submitChanges(eventEditMethod: EventEditMethod) = viewModelScope.launch {
        val currentUiState = uiState.value

        val latitude = currentUiState.locationLatitude
        val longitude = currentUiState.locationLongitude

        val location =
            if (latitude != null && longitude != null && currentUiState.isLocationEnabled) {
                Location(latitude, longitude)
            } else {
                null
            }

        when (eventEditMethod) {
            EventEditMethod.EditOne -> {
                val newRemindersTimeMillis = if (currentUiState.isReminderEnabled) {
                    currentUiState.reminderOffsets.map { reminderOffsetMillis ->
                        currentUiState.start + reminderOffsetMillis
                    }
                } else {
                    emptyList()
                }

                eventRepository.update(
                    Event(
                        id = eventId,
                        summary = currentUiState.summary,
                        type = currentUiState.type,
                        location = location,
                        start = currentUiState.start,
                        end = currentUiState.end,
                        recurrence = originalEvent?.recurrence,
                        reminder = newRemindersTimeMillis,
                        graded = currentUiState.isGraded,
                        grade = originalEvent?.grade,
                        maxGrade = originalEvent?.maxGrade
                    )
                )
            }

            EventEditMethod.EditAll -> {
                val eventsToBeUpdated = eventRepository.getAllBySummaryAndType(
                    originalEvent!!.summary,
                    originalEvent!!.type
                )

                eventsToBeUpdated.forEach { event ->
                    // Calculate new event starting time using event's epoch day +
                    // hour and minute offset entered by user
                    val newEventStartTimeMillis =
                        event.start.toEpochDay() * MILLISECONDS_IN_DAY +
                                currentUiState.start % MILLISECONDS_IN_DAY

                    // Calculate new event ending time using event's epoch day +
                    // hour and minute offset entered by user
                    val newEventEndTimeMillis =
                        event.end.toEpochDay() * MILLISECONDS_IN_DAY +
                                currentUiState.end % MILLISECONDS_IN_DAY

                    val newRemindersTimeMillis = if (currentUiState.isReminderEnabled) {
                        currentUiState.reminderOffsets.map { reminderOffsetMillis ->
                            newEventStartTimeMillis + reminderOffsetMillis
                        }
                    } else {
                        emptyList()
                    }

                    eventRepository.update(
                        Event(
                            id = event.id,
                            summary = currentUiState.summary,
                            type = currentUiState.type,
                            location = location,
                            start = newEventStartTimeMillis,
                            end = newEventEndTimeMillis,
                            recurrence = originalEvent?.recurrence,
                            reminder = newRemindersTimeMillis,
                            graded = currentUiState.isGraded,
                            grade = event.grade,
                            maxGrade = event.maxGrade
                        )
                    )
                }
            }
        }

        _sideEffectChannel.trySend(SideEffect.ShowToast(R.string.changes_saved))
        _sideEffectChannel.trySend(SideEffect.NavigateBack)
    }

    fun deleteEvent(eventDeleteMethod: EventDeleteMethod) {
        if (originalEvent == null) {
            return
        }
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
                                originalEvent!!.summary,
                                originalEvent!!.type
                            ).filter { it.start >= uiState.value.start }
                                .map { it.id }

                            eventRepository.delete(eventIdsToBeDeleted)
                            _sideEffectChannel.trySend(SideEffect.NavigateBack)
                        }

                        EventDeleteMethod.DeleteAll -> {
                            val eventIdsToBeDeleted = eventRepository.getAllBySummaryAndType(
                                originalEvent!!.summary,
                                originalEvent!!.type
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