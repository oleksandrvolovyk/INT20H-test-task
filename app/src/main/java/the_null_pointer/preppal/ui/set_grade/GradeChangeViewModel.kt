package the_null_pointer.preppal.ui.set_grade

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import the_null_pointer.preppal.data.Event
import the_null_pointer.preppal.data.EventRepository
import the_null_pointer.preppal.ui.SideEffect
import javax.inject.Inject

data class GradesChangeScreenUiState(
    val event: Event = Event.Empty
)

@HiltViewModel
class GradeChangeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val eventRepository: EventRepository
) : ViewModel() {

    private val eventId: Long = savedStateHandle.get<String>("id")!!.toLong()

    val uiState: StateFlow<GradesChangeScreenUiState> = eventRepository.observeEventById(eventId)
        .map {
            if (it != null) {
                GradesChangeScreenUiState(it)
            } else {
                // Event was removed from the database, so navigate back
                _sideEffectChannel.trySend(SideEffect.NavigateBack)
                GradesChangeScreenUiState()
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            GradesChangeScreenUiState()
        )

    private val _sideEffectChannel = Channel<SideEffect>(capacity = Channel.BUFFERED)
    val sideEffectFlow: Flow<SideEffect>
        get() = _sideEffectChannel.receiveAsFlow()

    fun updateCurrentGrade(currentGrade: String) = viewModelScope.launch {
        eventRepository.setEventGrade(eventId, currentGrade.toDoubleOrNull())
    }

    fun updateMaxGrade(maxGrade: String) = viewModelScope.launch {
        eventRepository.setEventMaxGrade(eventId, maxGrade.toDouble())
    }

    fun updateEventCompletion(completed: Boolean) = viewModelScope.launch {
        eventRepository.setEventCompletion(eventId, completed)
    }
}
