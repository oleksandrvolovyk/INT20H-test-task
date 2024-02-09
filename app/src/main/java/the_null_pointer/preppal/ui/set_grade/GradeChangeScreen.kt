package the_null_pointer.preppal.ui.set_grade

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import the_null_pointer.preppal.R
import the_null_pointer.preppal.data.Event
import the_null_pointer.preppal.data.Event.Type.Companion.completionStringResourceId
import the_null_pointer.preppal.data.Event.Type.Companion.stringResourceId
import the_null_pointer.preppal.ui.widget.CheckboxWithoutPadding
import the_null_pointer.preppal.util.TimeUtil.getReadableDate

@Composable
fun GradeChangeScreen(
    uiState: GradesChangeScreenUiState,
    onBackClicked: () -> Unit = {},
    onCurrentGradeValueChange: (String) -> Unit = {},
    onMaxGradeValueChange: (String) -> Unit = {},
    onEventCompletionChange: (Boolean) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
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
                Text(stringResource(uiState.event.type.stringResourceId))
            }
        }

        SimpleText(stringResource(uiState.event.type.stringResourceId))
        SimpleText(uiState.event.start.getReadableDate())
        SimpleText(uiState.event.summary)

        // Display current and max grades
        if (uiState.event.graded) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(start = 4.dp),
                    value = (uiState.event.grade ?: 0.0).toString(),
                    onValueChange = onCurrentGradeValueChange,
                    placeholder = {
                        Text(
                            text = stringResource(R.string.get_grade),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    )
                )
                Spacer(modifier = Modifier.width(16.dp))

                OutlinedTextField(
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(end = 4.dp),
                    value = (uiState.event.maxGrade ?: 0.0).toString(),
                    onValueChange = onMaxGradeValueChange,
                    placeholder = {
                        Text(
                            text = stringResource(R.string.max_grade),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    singleLine = true
                )
            }
        }

        if (uiState.event.completed != null) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(stringResource(uiState.event.type.completionStringResourceId))

                CheckboxWithoutPadding(
                    checked = uiState.event.completed,
                    onCheckedChange = { onEventCompletionChange(it) }
                )
            }
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

    val uiState = GradesChangeScreenUiState(
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

    // Call the GradeChangeScreen composable function with the mock data
    GradeChangeScreen(uiState = uiState)
}
