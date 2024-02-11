package the_null_pointer.preppal.ui.event.add_new

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
import the_null_pointer.preppal.data.event.EventRepository
import the_null_pointer.preppal.data.event.model.Event
import the_null_pointer.preppal.data.event.model.Location
import the_null_pointer.preppal.data.event.model.TimestampMillis
import the_null_pointer.preppal.data.location_name.LocationNameDatasource
import the_null_pointer.preppal.ui.SideEffect
import the_null_pointer.preppal.ui.event.BaseEventScreenUiState
import the_null_pointer.preppal.util.TimeUtil.MILLISECONDS_IN_DAY
import the_null_pointer.preppal.util.TimeUtil.MILLISECONDS_IN_HOUR
import the_null_pointer.preppal.util.TimeUtil.isWeekend
import the_null_pointer.preppal.util.TimeUtil.isWorkingDay
import java.util.Calendar
import java.util.Date
import java.util.TimeZone
import javax.inject.Inject

data class NewEventScreenUiState(
    override val summary: String = "",
    override val type: Event.Type = Event.Type.Task,
    override val recurrenceType: Event.RecurrenceType? = null,
    override val recurrenceEndDate: TimestampMillis = System.currentTimeMillis(),
    override val start: TimestampMillis = System.currentTimeMillis(),
    override val end: TimestampMillis = System.currentTimeMillis(),
    override val isReminderEnabled: Boolean = false,
    override val reminderOffsets: List<TimestampMillis> = emptyList(),

    override val isLocationEnabled: Boolean = false,
    override val locationName: String? = null,
    override val locationLatitude: Double? = null,
    override val locationLongitude: Double? = null,

    override val isGraded: Boolean = false
) : BaseEventScreenUiState

@HiltViewModel
class NewEventViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val eventRepository: EventRepository,
    private val locationNameDatasource: LocationNameDatasource
) : ViewModel() {

    private val timezone = TimeZone.getDefault()

    private val timezoneOffset = if (timezone.inDaylightTime(Date())) {
        timezone.rawOffset + timezone.dstSavings
    } else {
        timezone.rawOffset
    }

    private val startingEpochDay = savedStateHandle.get<Long>("startingEpochDay")
    private val startingTimestampMillis = if (startingEpochDay != null) {
        // Day in millis + Hour and minutes in millis
        startingEpochDay * MILLISECONDS_IN_DAY + (Calendar.getInstance().timeInMillis + timezoneOffset) % MILLISECONDS_IN_DAY
    } else {
        System.currentTimeMillis()
    }

    private val _uiState = MutableStateFlow(
        NewEventScreenUiState(
            start = startingTimestampMillis,
            end = startingTimestampMillis + MILLISECONDS_IN_HOUR,
            recurrenceEndDate = startingTimestampMillis
        )
    )
    val uiState: StateFlow<NewEventScreenUiState> = _uiState.asStateFlow()

    private val _sideEffectChannel = Channel<SideEffect>(capacity = Channel.BUFFERED)
    val sideEffectFlow: Flow<SideEffect>
        get() = _sideEffectChannel.receiveAsFlow()

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
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    locationName = locationNameDatasource.getDisplayNameForLocation(
                        latitude,
                        longitude
                    )
                )
            }
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
        val currentUiState = uiState.value

        val latitude = currentUiState.locationLatitude
        val longitude = currentUiState.locationLongitude

        val location =
            if (latitude != null && longitude != null && currentUiState.isLocationEnabled) {
                Location(latitude, longitude)
            } else {
                null
            }

        // Verify summary is not blank
        if (currentUiState.summary.isBlank()) {
            _sideEffectChannel.trySend(
                SideEffect.ShowToast(R.string.summary_must_not_be_blank)
            )
            return@launch
        }

        // Verify summary and type combination is unique
        if (eventRepository.getAllBySummaryAndType(currentUiState.summary, currentUiState.type)
                .isNotEmpty()
        ) {
            _sideEffectChannel.trySend(
                SideEffect.ShowToast(R.string.event_with_summary_and_type_already_exists)
            )
            return@launch
        }

        // Verify start <= end
        if (currentUiState.start > currentUiState.end) {
            _sideEffectChannel.trySend(
                SideEffect.ShowToast(R.string.start_time_greater_than_end_time)
            )
            return@launch
        }

        // If Event is recurrent, create n needed events, instead of one
        val events = arrayListOf<Event>()

        val completedFieldValue = if (currentUiState.type in Event.Type.CompletableTypes) {
            false
        } else {
            null
        }

        val baseEvent = Event(
            summary = currentUiState.summary,
            type = currentUiState.type,
            location = location,
            locationName = if (currentUiState.isLocationEnabled) currentUiState.locationName else null,
            start = currentUiState.start,
            end = currentUiState.end,
            recurrence = currentUiState.recurrenceType,
            reminder = emptyList(),
            graded = currentUiState.isGraded,
            completed = completedFieldValue
        )

        if (currentUiState.recurrenceType != null) {
            when (currentUiState.recurrenceType) {
                Event.RecurrenceType.Daily -> {
                    events.addAll(
                        generateRecurrentEvents(
                            baseEvent = baseEvent,
                            recurrenceEndDayMillis = currentUiState.recurrenceEndDate
                        )
                    )
                }

                Event.RecurrenceType.EveryWorkDay -> {
                    events.addAll(
                        generateRecurrentEvents(
                            baseEvent = baseEvent,
                            recurrenceEndDayMillis = currentUiState.recurrenceEndDate,
                            dayCondition = { it.isWorkingDay() }
                        )
                    )
                }

                Event.RecurrenceType.EveryWeekend -> {
                    events.addAll(
                        generateRecurrentEvents(
                            baseEvent = baseEvent,
                            recurrenceEndDayMillis = currentUiState.recurrenceEndDate,
                            dayCondition = { it.isWeekend() }
                        )
                    )
                }

                Event.RecurrenceType.Weekly -> {
                    events.addAll(
                        generateRecurrentEvents(
                            baseEvent = baseEvent,
                            recurrenceEndDayMillis = currentUiState.recurrenceEndDate,
                            dayStep = 7
                        )
                    )
                }

                Event.RecurrenceType.Monthly -> {
                    events.addAll(
                        generateRecurrentEvents(
                            baseEvent = baseEvent,
                            recurrenceEndDayMillis = currentUiState.recurrenceEndDate,
                            dayStep = 30
                        )
                    )
                }
            }
        } else {
            events.add(baseEvent)
        }

        val eventsWithReminders = events.map { event ->
            event.copy(
                reminder = currentUiState.reminderOffsets.map { reminderOffsetMillis ->
                    event.start + reminderOffsetMillis
                }
            )
        }

        if (eventRepository.insertAll(eventsWithReminders)) {
            _sideEffectChannel.trySend(SideEffect.ShowToast(R.string.event_successfully_saved))
            _sideEffectChannel.trySend(SideEffect.NavigateBack)
        } else {
            _sideEffectChannel.trySend(SideEffect.ShowToast(R.string.event_failed_to_save))
        }
    }

    /**
     * Generate a list of events based of baseEvent's recurrence type and recurrenceEndDayMillis.
     * @param baseEvent                 Event that will be used in generation.
     * @param recurrenceEndDayMillis    Day in milliseconds when recurrence ends (inclusive)
     * @param dayStep                   Number of days to skip until next Event. 1 by default
     * @param dayCondition              Can be applied to filter events (for example, to create events only on working days)
     * @return                          Hour of day (24-hour format).
     */
    private fun generateRecurrentEvents(
        baseEvent: Event,
        recurrenceEndDayMillis: Long,
        dayStep: Int = 1,
        dayCondition: ((dayMillis: TimestampMillis) -> Boolean)? = null
    ): List<Event> {
        val startDayMillis = baseEvent.start - baseEvent.start % MILLISECONDS_IN_DAY

        val events = arrayListOf<Event>()
        var currentDayOffsetMillis = 0L

        while (startDayMillis + currentDayOffsetMillis <= recurrenceEndDayMillis) {
            if (dayCondition?.invoke(startDayMillis + currentDayOffsetMillis) != false) {
                events.add(
                    baseEvent.copy(
                        start = baseEvent.start + currentDayOffsetMillis,
                        end = baseEvent.end + currentDayOffsetMillis,
                    )
                )
            }
            currentDayOffsetMillis += dayStep * MILLISECONDS_IN_DAY
        }

        return events
    }
}