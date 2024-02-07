package the_null_pointer.preppal.ui.grades_by_type

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun GradesByType(viewModel: GradesByTypeViewModel = hiltViewModel(), onGradeClick: () -> Unit, onBackClick: () -> Unit, type: String ) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect( key1 = type){
        viewModel.updateType(type)
    }

    GradesByTypeScreen(uiState = uiState, onGradeClick = onGradeClick, onBackClick = onBackClick)
}
