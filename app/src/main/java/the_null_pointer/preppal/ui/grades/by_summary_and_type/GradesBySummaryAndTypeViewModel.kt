package the_null_pointer.preppal.ui.grades.by_summary_and_type

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import the_null_pointer.preppal.data.Event
import the_null_pointer.preppal.data.EventRepository
import the_null_pointer.preppal.util.TimeUtil.getReadableTimeAndDate
import javax.inject.Inject

data class GradesBySummaryAndTypeScreenUiState(
    val eventSummary: String,
    val eventType: Event.Type,
    val progressBySummaryAndTypeListItems: List<GradesBySummaryAndTypeListItem> = emptyList()
)

data class GradesBySummaryAndTypeListItem(
    val eventId: Long,
    val time: String,
    val grade: Double?,
    val maxGrade: Double?
)

@HiltViewModel
class GradesBySummaryAndTypeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val eventRepository: EventRepository
) : ViewModel() {

    private val eventSummary = savedStateHandle.get<String>("summary")!!
    private val eventType = Event.Type.valueOf(savedStateHandle.get<String>("type")!!)

    val uiState: StateFlow<GradesBySummaryAndTypeScreenUiState> = eventRepository.observeEvents()
        .map {
            it
                .filter { event -> event.graded }
                .filter { event -> event.summary == eventSummary }
                .filter { event -> event.type == eventType }
        }
        .map { eventsOfType ->
            GradesBySummaryAndTypeScreenUiState(
                eventSummary = eventSummary,
                eventType = eventType,
                progressBySummaryAndTypeListItems = eventsOfType.mapToGradesBySummaryAndTypeListItems()
            )
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            GradesBySummaryAndTypeScreenUiState(
                eventSummary = eventSummary,
                eventType = eventType
            )
        )

    private fun List<Event>.mapToGradesBySummaryAndTypeListItems(): List<GradesBySummaryAndTypeListItem> =
        this.sortedBy { it.start }
            .map { event ->
                GradesBySummaryAndTypeListItem(
                    eventId = event.id,
                    time = event.start.getReadableTimeAndDate(),
                    grade = event.grade,
                    maxGrade = event.maxGrade
                )
            }
}