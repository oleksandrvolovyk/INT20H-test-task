package the_null_pointer.preppal.ui.grades_by_type

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

import javax.inject.Inject

data class GradesByTypeScreenUiState(
    val events: List<Event> = emptyList()
)

@HiltViewModel
class GradesByTypeViewModel @Inject constructor(private val eventRepository: EventRepository
    ): ViewModel() {

    private val _uiState = MutableStateFlow(GradesByTypeScreenUiState())
    val uiState: StateFlow<GradesByTypeScreenUiState> = _uiState.asStateFlow()


    fun updateType(type: String) = viewModelScope.launch{
        _uiState.update{
            it.copy(events = eventRepository.getAllByType(type))
        }
    }
}