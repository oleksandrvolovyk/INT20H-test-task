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
import the_null_pointer.preppal.data.EventRepository
import javax.inject.Inject

data class GradesChangeScreenUiState(
    val event: Event? = null
)

@HiltViewModel
class GradeChangeViewModel @Inject constructor(private val eventRepository: EventRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(GradesChangeScreenUiState())
    val uiState: StateFlow<GradesChangeScreenUiState> = _uiState.asStateFlow()

}