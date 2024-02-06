package the_null_pointer.preppal.ui.new_event

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewEvent(viewModel: NewEventViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    NewEventScreen(
        uiState = uiState,
        onSummaryValueChange = { viewModel.updateSummary(it) },
        onEventTypeChange = { viewModel.updateEventType(it) },
        onStartDateChange = { viewModel.updateStartDate(it) },
        onEndDateChange = { viewModel.updateEndDate(it) },
        onSubmitEventButtonClick = { viewModel.submitEvent() }
    )
}
