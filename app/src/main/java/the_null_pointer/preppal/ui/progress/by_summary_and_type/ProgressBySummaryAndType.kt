package the_null_pointer.preppal.ui.progress.by_summary_and_type

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import the_null_pointer.preppal.ui.SideEffect
import the_null_pointer.preppal.ui.SingleEventEffect
import the_null_pointer.preppal.ui.handleSideEffect

@Composable
fun ProgressBySummaryAndType(
    viewModel: ProgressBySummaryAndTypeViewModel = hiltViewModel(),
    onEventClick: (eventId: Long) -> Unit,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    ProgressBySummaryAndTypeScreen(
        uiState = uiState,
        onEventClick = onEventClick,
        onEventCompletionChange = { eventId, completed ->
            viewModel.updateEventCompletion(
                eventId,
                completed
            )
        },
        onBackClick = onBackClick,
        onUpdateEducationalResourceFavourite = { resourceId, favourite ->
            viewModel.updateEducationalResourceFavourite(resourceId, favourite)
        },
        onDeleteEducationalResource = { viewModel.deleteEducationalResource(it) },
        onAddEducationalResourceButtonClick = { viewModel.startAddingNewEducationalResource() },
        onNewEducationalResourceTypeChange = { viewModel.updateNewEducationalResourceType(it) },
        onNewEducationalResourceNameChange = { viewModel.updateNewEducationalResourceName(it) },
        onNewEducationalResourceLinkChange = { viewModel.updateNewEducationalResourceLink(it) },
        onNewEducationalResourceSubmitClick = { viewModel.submitNewEducationalResource() },
        onNewEducationalResourceCancelClick = { viewModel.cancelAddingNewEducationalResource() }
    )

    SingleEventEffect(sideEffectFlow = viewModel.sideEffectFlow) { sideEffect ->
        if (!handleSideEffect(context, sideEffect)) {
            if (sideEffect is SideEffect.NavigateBack) {
                onBackClick()
            }
        }
    }
}
