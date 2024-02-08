package the_null_pointer.preppal.ui.set_grade

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun GradeChange(viewModel: GradeChangeViewModel = hiltViewModel(), eventId:String, onBackClicked: () -> Unit) {

    val uiState by viewModel.uiState.collectAsState()
    val eventDetails by  viewModel.eventDetails.collectAsState() // Access the eventDetails directly

    LaunchedEffect(key1 = eventId) {
        viewModel.getEvents(eventId.toLong())
    }

    GradeChangeScreen(
        uiState = uiState,
        eventDetails = eventDetails,
        onBackClicked = onBackClicked,
        onCurrentGradeValueChange = { viewModel.updateCurrentGrade(it) },
        onMaxGradeValueChange = { viewModel.updateMaxGrade(it) },
    )}