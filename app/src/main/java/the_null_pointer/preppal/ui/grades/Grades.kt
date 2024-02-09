package the_null_pointer.preppal.ui.grades

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun Grades(
    viewModel: GradesViewModel = hiltViewModel(),
    onGradesTypeClick: (String) -> Unit,
    onProgressTypeClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    GradesScreen(
        uiState = uiState,
        onGradesTypeClick = onGradesTypeClick,
        onProgressTypeClick = onProgressTypeClick
    )
}


