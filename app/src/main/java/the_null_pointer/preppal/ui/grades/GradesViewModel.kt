package the_null_pointer.preppal.ui.grades

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

data class GradesScreenUiState(
    val gradeListItems: List<GradeListItem> = emptyList(),
    val progressListItems: List<ProgressListItem> = emptyList()
)

data class GradeListItem(
    val eventType: Event.Type,
    val gradedCount: Int,
    val totalCount: Int
)

data class ProgressListItem(
    val eventType: Event.Type,
    val completedCount: Int,
    val totalCount: Int
)

@HiltViewModel
class GradesViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {
    val uiState: StateFlow<GradesScreenUiState> = eventRepository.observeEvents()
        .map { events ->
            GradesScreenUiState(
                gradeListItems = events.mapToGradeListItems(),
                progressListItems = events.mapToProgressListItems()
            )
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            GradesScreenUiState()
        )

    private fun List<Event>.mapToGradeListItems(): List<GradeListItem> = this
        .filter { it.graded }
        .groupBy { it.type }
        .map { (eventType, eventsOfType) ->
            GradeListItem(
                eventType = eventType,
                gradedCount = eventsOfType.count { it.grade != null },
                totalCount = eventsOfType.size
            )
        }

    private fun List<Event>.mapToProgressListItems(): List<ProgressListItem> = this
        .filter { it.completed != null }
        .groupBy { it.type }
        .map { (eventType, eventsOfType) ->
            ProgressListItem(
                eventType = eventType,
                completedCount = eventsOfType.count { it.completed == true },
                totalCount = eventsOfType.size
            )
        }
}