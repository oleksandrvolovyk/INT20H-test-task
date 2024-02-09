package the_null_pointer.preppal.ui.progress.by_summary_and_type

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ProgressBySummaryAndType(
    viewModel: ProgressBySummaryAndTypeViewModel = hiltViewModel(),
    onEventClick: (eventId: Long) -> Unit,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    ProgressBySummaryAndTypeScreen(
        uiState = uiState,
        onEventClick = onEventClick,
        onEventCompletionChange = { eventId, completed ->
            viewModel.updateEventCompletion(
                eventId,
                completed
            )
        },
        onBackClick = onBackClick
    )
}
