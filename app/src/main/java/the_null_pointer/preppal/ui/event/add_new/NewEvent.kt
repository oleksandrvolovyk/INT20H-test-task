package the_null_pointer.preppal.ui.event.add_new

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import the_null_pointer.preppal.ui.SideEffect
import the_null_pointer.preppal.ui.SingleEventEffect
import the_null_pointer.preppal.ui.handleSideEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewEvent(viewModel: NewEventViewModel = hiltViewModel(), onNavigateBack: () -> Unit) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    NewEventScreen(
        uiState = uiState,
        onSummaryValueChange = { viewModel.updateSummary(it) },
        onEventTypeChange = { viewModel.updateEventType(it) },
        onEventRecurrenceTypeChange = { viewModel.updateRecurrenceType(it) },
        onRecurrenceEndDateChange = { viewModel.updateRecurrenceEndDate(it) },
        onStartDateChange = { viewModel.updateStartDate(it) },
        onEndDateChange = { viewModel.updateEndDate(it) },
        onReminderStateChange = { viewModel.updateReminderState(it) },
        onReminderOffsetsChange = { viewModel.updateReminders(it) },
        onLocationStateChange = { viewModel.updateLocationState(it) },
        onLocationChange = { latitude, longitude -> viewModel.updateLocation(latitude, longitude) },
        onGradedChange = { viewModel.updateGradedState(it) },
        onSubmitEventButtonClick = { viewModel.submitEvent() }
    )

    SingleEventEffect(sideEffectFlow = viewModel.sideEffectFlow) { sideEffect ->
        if (!handleSideEffect(context, sideEffect)) {
            if (sideEffect is SideEffect.NavigateBack) {
                onNavigateBack()
            }
        }
    }
}
