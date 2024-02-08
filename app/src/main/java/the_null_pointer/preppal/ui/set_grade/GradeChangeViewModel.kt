package the_null_pointer.preppal.ui.set_grade

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import the_null_pointer.preppal.data.Event
import the_null_pointer.preppal.data.Event.Type.Companion.stringResourceId
import the_null_pointer.preppal.data.EventRepository
import the_null_pointer.preppal.util.TimeUtil.getReadableDate
import javax.inject.Inject

data class GradesChangeScreenUiState(
    val eventType: Int = 0,
    val eventSummary: String = "",
    val eventDate: String = "",
    val currentGrade: Int = 0,
    val maxGrade: Int = 0
)

@HiltViewModel
class GradeChangeViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {

    private val _eventDetails = MutableStateFlow(GradesChangeScreenUiState())
    val eventDetails: StateFlow<GradesChangeScreenUiState> = _eventDetails.asStateFlow()

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState
    fun getEvents(id: Long) {
        viewModelScope.launch {
            try {
                val event = eventRepository.getAllById(id)
                _uiState.value = UiState.Success(event)
                // Process the data and update individual StateFlows

                _eventDetails.update { it.copy(eventType = event.type.stringResourceId) }
                _eventDetails.update {it.copy(eventSummary =  event.summary)}
                _eventDetails.update {it.copy(eventDate =  event.end.getReadableDate())}

        } catch (e: Exception) {
            _uiState.value = UiState.Error(e.message ?: "Unknown error")
        }
    }
}
    fun updateCurrentGrade(currentGrade: String) {
        _eventDetails.update {
            it.copy(currentGrade = currentGrade.toInt() )
        }
    }

    fun updateMaxGrade(maxGrade: String) {
        _eventDetails.update {
            it.copy(maxGrade =  maxGrade.toInt())
        }
    }
}

sealed class UiState {
    object Loading : UiState()
    data class Success(val event: Event) : UiState()
    data class Error(val message: String) : UiState()

}
