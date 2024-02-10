package the_null_pointer.preppal.ui.grades.by_type

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import the_null_pointer.preppal.data.event.model.Event
import the_null_pointer.preppal.data.event.EventRepository
import javax.inject.Inject

data class GradesByTypeScreenUiState(
    val eventType: Event.Type,
    val gradesByTypeListItems: List<GradesByTypeListItem> = emptyList()
)

data class GradesByTypeListItem(
    val eventSummary: String,
    val gradedCount: Int,
    val totalCount: Int
)

@HiltViewModel
class GradesByTypeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val eventRepository: EventRepository
) : ViewModel() {

    private val eventType = Event.Type.valueOf(savedStateHandle.get<String>("type")!!)

    val uiState: StateFlow<GradesByTypeScreenUiState> = eventRepository.observeEvents()
        .map {
            it
                .filter { event -> event.graded }
                .filter { event -> event.type == eventType }
        }
        .map { eventsOfType ->
            GradesByTypeScreenUiState(
                eventType = eventType,
                gradesByTypeListItems = eventsOfType.mapToGradesByTypeListItems()
            )
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            GradesByTypeScreenUiState(eventType = eventType)
        )

    private fun List<Event>.mapToGradesByTypeListItems(): List<GradesByTypeListItem> = this
        .groupBy { it.summary }
        .map { (eventSummary, eventsOfSummary) ->
            GradesByTypeListItem(
                eventSummary = eventSummary,
                gradedCount = eventsOfSummary.count { it.grade != null },
                totalCount = eventsOfSummary.size
            )
        }
}