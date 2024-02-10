package the_null_pointer.preppal.ui.grades.by_summary_and_type

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun GradesBySummaryAndType(
    viewModel: GradesBySummaryAndTypeViewModel = hiltViewModel(),
    onEventClick: (eventId: Long) -> Unit,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    GradesBySummaryAndTypeScreen(
        uiState = uiState,
        onEventClick = onEventClick,
        onBackClick = onBackClick
    )
}