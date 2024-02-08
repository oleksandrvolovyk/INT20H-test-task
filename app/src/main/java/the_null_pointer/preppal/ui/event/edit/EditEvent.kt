package the_null_pointer.preppal.ui.event.edit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import the_null_pointer.preppal.ui.SideEffect
import the_null_pointer.preppal.ui.SingleEventEffect
import the_null_pointer.preppal.ui.handleSideEffect

@Composable
fun EditEvent(
    viewModel: EditEventViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    EditEventScreen(
        uiState = uiState,
        onSummaryValueChange = { viewModel.updateSummary(it) },
        onEventTypeChange = { viewModel.updateEventType(it) },
        onStartDateChange = { viewModel.updateStartDate(it) },
        onEndDateChange = { viewModel.updateEndDate(it) },
        onReminderStateChange = { viewModel.updateReminderState(it) },
        onReminderOffsetsChange = { viewModel.updateReminders(it) },
        onLocationStateChange = { viewModel.updateLocationState(it) },
        onLocationChange = { latitude, longitude -> viewModel.updateLocation(latitude, longitude) },
        onGradedChange = { viewModel.updateGradedState(it) },
        onSubmitChangesButtonClick = { viewModel.submitChanges(it) },
        onDeleteEventButtonClick = { viewModel.deleteEvent(it) }
    )

    SingleEventEffect(sideEffectFlow = viewModel.sideEffectFlow) { sideEffect ->
        if (!handleSideEffect(context, sideEffect)) {
            if (sideEffect is SideEffect.NavigateBack) {
                onNavigateBack()
            }
        }
    }
}
