package the_null_pointer.preppal.ui.grades

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import the_null_pointer.preppal.data.Event
import the_null_pointer.preppal.data.EventRepository
import javax.inject.Inject

data class GradesScreenUiState(
    val events: List<Event> = emptyList()
)

@HiltViewModel
class GradesViewModel @Inject constructor(private val eventRepository: EventRepository): ViewModel() {
    val uiState: StateFlow<GradesScreenUiState> = eventRepository.observeEvents()
        .map {   events ->
            val uniqueEventTypes = HashSet<String>()
            val filteredEvents = events.filter { event ->
                val isUnique = uniqueEventTypes.add(event.type.toString())
                isUnique
            }

            // Create GradesScreenUiState with filtered events
            GradesScreenUiState(filteredEvents)
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            GradesScreenUiState()
        )
}