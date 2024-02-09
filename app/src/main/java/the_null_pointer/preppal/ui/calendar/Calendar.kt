package the_null_pointer.preppal.ui.calendar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun Calendar(
    viewModel: CalendarViewModel = hiltViewModel(),
    onNewEventButtonClick: (selectedEpochDay: Long?) -> Unit,
    onEventClick: (eventId: Long) -> Unit,
    onEventGradeClick: (eventId: Long) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    CalendarScreen(
        uiState = uiState,
        onNewEventButtonClick = onNewEventButtonClick,
        onEventClick = onEventClick,
        onEventCompletionChange = { eventId, completed ->
            viewModel.updateEventCompletion(
                eventId,
                completed
            )
        },
        onEventGradeClick = onEventGradeClick
    )
}
