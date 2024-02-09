package the_null_pointer.preppal.ui.set_grade

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import the_null_pointer.preppal.ui.SideEffect
import the_null_pointer.preppal.ui.SingleEventEffect
import the_null_pointer.preppal.ui.handleSideEffect

@Composable
fun GradeChange(
    viewModel: GradeChangeViewModel = hiltViewModel(),
    onBackClicked: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    GradeChangeScreen(
        uiState = uiState,
        onBackClicked = onBackClicked,
        onCurrentGradeValueChange = { viewModel.updateCurrentGrade(it) },
        onMaxGradeValueChange = { viewModel.updateMaxGrade(it) },
        onEventCompletionChange = { viewModel.updateEventCompletion(it) }
    )

    SingleEventEffect(sideEffectFlow = viewModel.sideEffectFlow) { sideEffect ->
        if (!handleSideEffect(context, sideEffect)) {
            if (sideEffect is SideEffect.NavigateBack) {
                onBackClicked()
            }
        }
    }
}