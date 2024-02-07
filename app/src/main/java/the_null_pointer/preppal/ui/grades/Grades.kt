package the_null_pointer.preppal.ui.grades

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
@Composable
fun Grades(viewModel: GradesViewModel = hiltViewModel(), onTypeClick: () -> Unit) {
    val uiState by viewModel.uiState.collectAsState()

    GradesScreen( uiState = uiState, onTypeClick = onTypeClick)
}
