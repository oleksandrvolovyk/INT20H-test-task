package the_null_pointer.preppal.ui.set_grade

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import the_null_pointer.preppal.R
import the_null_pointer.preppal.data.Event
import the_null_pointer.preppal.data.Location


@Composable
fun GradeChangeScreen(
    uiState: UiState,
    eventDetails: GradesChangeScreenUiState,
    onBackClicked: () -> Unit = {},
    onCurrentGradeValueChange: (String) -> Unit = {},
    onMaxGradeValueChange: (String) -> Unit = {}
) {

    var type = remember { mutableStateOf("") }
    var summary = remember { mutableStateOf("") }
    var date = remember { mutableStateOf("") }

    when (uiState) {
        is UiState.Loading -> {
            Log.d("GradeChangeScreen", "Load")
        }

        is UiState.Success -> {
            type.value = stringResource(eventDetails.eventType)
            summary.value = eventDetails.eventSummary
            date.value = eventDetails.eventDate
        }

        is UiState.Error -> {
            val errorMessage = (uiState as UiState.Error).message

        }
    }

    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                state = scrollState,
                enabled = true
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    modifier = Modifier
                        .width(65.dp)
                        .height(35.dp)
                        .align(Alignment.TopStart),
                    onClick = onBackClicked,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_left),
                        contentDescription = "Arrow icon"
                    )
                }
                Text(text = type.value)
            }
        }

        SimpleText(text = type.value)
        SimpleText(text = date.value)
        SimpleText(text = summary.value)

        // Display current and max grades
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedTextField(
                modifier = Modifier.weight(0.5f),
                value = if (eventDetails.currentGrade == 0) "" else "${eventDetails.currentGrade}",
                onValueChange = onCurrentGradeValueChange,
                placeholder = { Text(text = stringResource(id = R.string.get_grade)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                )
            )
            Spacer(modifier = Modifier.width(16.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth().weight(0.5f),
                placeholder = { Text(stringResource(R.string.max_grade)) },
                value = if (eventDetails.maxGrade == 0) "" else "${eventDetails.maxGrade}",
                onValueChange = onMaxGradeValueChange,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                singleLine = true
            )

        }
    }
}




@Composable
fun SimpleText(
    text: String
) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
//            .clip(shape = CutCornerShape(1.dp))
            .padding(6.dp),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colors.onPrimary
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        )

    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                modifier = Modifier
                    .padding(4.dp)

            )
        }
    }
}

@Preview
@Composable
fun GradesPreview() {

    val uiState = UiState.Success(
        Event(
            id = 1,
            summary = "Lecture on Kotlin Basics",
            type = Event.Type.Lecture,
            location = null,
            start = System.currentTimeMillis(),
            end = System.currentTimeMillis() + 3600 * 1000,
            recurrence = null,
            reminder = emptyList(),
            graded = true,
            grade = 85.0,
            maxGrade = 100.0
        )
    )

    // Create a mock GradesChangeScreenUiState object for preview
    val eventDetails = GradesChangeScreenUiState(
        eventType = 0, // Replace with the appropriate value
        eventSummary = "Sample Summary", // Replace with the appropriate value
        eventDate = "Sample Date", // Replace with the appropriate value
        currentGrade = 0, // Replace with the appropriate value
        maxGrade = 0 // Replace with the appropriate value
    )

    // Call the GradeChangeScreen composable function with the mock data
    GradeChangeScreen(
        uiState = uiState,
        eventDetails = eventDetails,
        onBackClicked = {}
    )


    GradeChangeScreen(
        uiState =  uiState,
        eventDetails =  GradesChangeScreenUiState(),

        )
}
