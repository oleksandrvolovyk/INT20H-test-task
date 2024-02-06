package the_null_pointer.preppal.ui.calendar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun Calendar(viewModel: CalendarViewModel = hiltViewModel(), onNewEventButtonClick: () -> Unit) {
    val uiState by viewModel.uiState.collectAsState()

    CalendarScreen(uiState = uiState, onNewEventButtonClick = onNewEventButtonClick)
}
