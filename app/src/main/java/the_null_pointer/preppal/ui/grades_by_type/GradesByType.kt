package the_null_pointer.preppal.ui.grades_by_type

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import the_null_pointer.preppal.ui.grades_by_type.GradesByTypeScreen

@Composable
fun GradesByType(viewModel: GradesByTypeViewModel = hiltViewModel(), onGradeClick: () -> Unit, onBackClick: () -> Unit ) {
    val uiState by viewModel.uiState.collectAsState()

    GradesByTypeScreen(uiState = uiState, onGradeClick = onGradeClick, onBackClick = onBackClick)
}
