package the_null_pointer.preppal.ui.grades.by_type

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import the_null_pointer.preppal.data.Event

@Composable
fun GradesByType(
    viewModel: GradesByTypeViewModel = hiltViewModel(),
    onSummaryAndTypeClick: (summary: String, type: Event.Type) -> Unit,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    GradesByTypeScreen(
        uiState = uiState,
        onSummaryAndTypeClick = onSummaryAndTypeClick,
        onBackClick = onBackClick
    )
}
