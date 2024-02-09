package the_null_pointer.preppal.ui.progress.by_summary_and_type

import androidx.lifecycle.SavedStateHandle
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
import the_null_pointer.preppal.util.TimeUtil.getHourAsString
import the_null_pointer.preppal.util.TimeUtil.getMinuteAsString
import the_null_pointer.preppal.util.TimeUtil.getReadableDate
import javax.inject.Inject

data class ProgressBySummaryAndTypeScreenUiState(
    val eventSummary: String,
    val eventType: Event.Type,
    val progressBySummaryAndTypeListItems: List<ProgressBySummaryAndTypeListItem> = emptyList()
)

data class ProgressBySummaryAndTypeListItem(
    val eventId: Long,
    val time: String,
    val completed: Boolean
)

@HiltViewModel
class ProgressBySummaryAndTypeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val eventRepository: EventRepository
) : ViewModel() {

    private val eventSummary = savedStateHandle.get<String>("summary")!!
    private val eventType = Event.Type.valueOf(savedStateHandle.get<String>("type")!!)

    val uiState: StateFlow<ProgressBySummaryAndTypeScreenUiState> = eventRepository.observeEvents()
        .map {
            it
                .filter { event -> event.completed != null }
                .filter { event -> event.summary == eventSummary }
                .filter { event -> event.type == eventType }
        }
        .map { eventsOfType ->
            ProgressBySummaryAndTypeScreenUiState(
                eventSummary = eventSummary,
                eventType = eventType,
                progressBySummaryAndTypeListItems = eventsOfType.mapToProgressBySummaryAndTypeListItems()
            )
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            ProgressBySummaryAndTypeScreenUiState(
                eventSummary = eventSummary,
                eventType = eventType
            )
        )

    private fun List<Event>.mapToProgressBySummaryAndTypeListItems(): List<ProgressBySummaryAndTypeListItem> =
        this.sortedBy { it.start }
            .map { event ->
                ProgressBySummaryAndTypeListItem(
                    eventId = event.id,
                    time = "${event.start.getHourAsString()}:${event.start.getMinuteAsString()} ${event.start.getReadableDate()}",
                    completed = event.completed!!
                )
            }

    fun updateEventCompletion(eventId: Long, completed: Boolean) = viewModelScope.launch {
        eventRepository.setEventCompletion(eventId, completed)
    }
}