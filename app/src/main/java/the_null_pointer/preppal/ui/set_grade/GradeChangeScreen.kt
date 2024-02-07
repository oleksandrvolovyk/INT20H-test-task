package the_null_pointer.preppal.ui.set_grade

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.TextField
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
import the_null_pointer.preppal.data.Event.Type.Companion.stringResourceId
import the_null_pointer.preppal.util.TimeUtil.getReadableDate

@Composable
fun GradeChangeScreen(event: Event, onBackClicked: () -> Unit = {}) {
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
                Text(
                    text = "Оцінка - ${stringResource(id = event.type.stringResourceId)} "
                )
            }
        }

        SimpleText(text = stringResource(id = event.type.stringResourceId))
        SimpleText(text = event.end.getReadableDate())
        SimpleText(text = event.summary)

        // Move to ViewModel !!

        val getGradeState = remember { mutableStateOf("") }
        val maxGradeState = remember { mutableStateOf("") }

        Row {

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.5f),
                placeholder = { Text(stringResource(id = R.string.get_grade)) },
                value = getGradeState.value,
                onValueChange = { grade -> getGradeState.value = grade },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.width(5.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.5f),
                placeholder = { Text(text = stringResource(id = R.string.max_grade)) },
                value = maxGradeState.value,
                onValueChange = { grade -> maxGradeState.value = grade },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email,
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

    GradeChangeScreen(
        event = Event(
            summary = "summary",
            type = Event.Type.Exam,
            location = null,
            start = 12321443213,
            end = 1232312344423,

            graded = false,
            grade = 12.0,
            maxGrade = 15.0


        )
    )
}
