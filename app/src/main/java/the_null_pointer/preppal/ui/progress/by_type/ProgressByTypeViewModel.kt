package the_null_pointer.preppal.ui.progress.by_type

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

data class ProgressByTypeScreenUiState(
    val eventType: Event.Type,
    val progressByTypeListItems: List<ProgressByTypeListItem> = emptyList()
)

data class ProgressByTypeListItem(
    val eventSummary: String,
    val completedCount: Int,
    val totalCount: Int
)

@HiltViewModel
class ProgressByTypeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val eventRepository: EventRepository
) : ViewModel() {

    private val eventType = Event.Type.valueOf(savedStateHandle.get<String>("type")!!)

    val uiState: StateFlow<ProgressByTypeScreenUiState> = eventRepository.observeEvents()
        .map {
            it
                .filter { event -> event.completed != null }
                .filter { event -> event.type == eventType }
        }
        .map { eventsOfType ->
            ProgressByTypeScreenUiState(
                eventType = eventType,
                progressByTypeListItems = eventsOfType.mapToProgressByTypeListItems()
            )
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            ProgressByTypeScreenUiState(eventType = eventType)
        )

    private fun List<Event>.mapToProgressByTypeListItems(): List<ProgressByTypeListItem> = this
        .groupBy { it.summary }
        .map { (eventSummary, eventsOfSummary) ->
            ProgressByTypeListItem(
                eventSummary = eventSummary,
                completedCount = eventsOfSummary.count { it.completed == true },
                totalCount = eventsOfSummary.size
            )
        }
}