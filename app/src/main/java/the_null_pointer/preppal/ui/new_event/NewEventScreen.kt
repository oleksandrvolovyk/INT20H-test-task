package the_null_pointer.preppal.ui.new_event

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import the_null_pointer.preppal.R
import the_null_pointer.preppal.data.Event
import the_null_pointer.preppal.ui.theme.PrepPalTheme
import the_null_pointer.preppal.ui.widget.MyDatePickerDialog
import the_null_pointer.preppal.ui.widget.MyTimePickerDialog
import the_null_pointer.preppal.ui.widget.Spinner
import the_null_pointer.preppal.util.TimeUtil.getHour
import the_null_pointer.preppal.util.TimeUtil.getMinute
import the_null_pointer.preppal.util.TimeUtil.getReadableDate

private const val MILLISECONDS_IN_DAY = 86_400_000
private const val MILLISECONDS_IN_HOUR = 3_600_000
private const val MILLISECONDS_IN_MINUTE = 60_000

@ExperimentalMaterial3Api
@Composable
fun NewEventScreen(
    uiState: NewEventScreenUiState,
    onSummaryValueChange: (String) -> Unit = {},
    onEventTypeChange: (Event.Type) -> Unit = {},
    onStartDateChange: (Long) -> Unit = {},
    onEndDateChange: (Long) -> Unit = {},
    onSubmitEventButtonClick: () -> Unit = {}
) {
    Column {
        OutlinedTextField(
            value = uiState.summary,
            onValueChange = onSummaryValueChange,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            ),
            textStyle = TextStyle.Default.copy(
                color = MaterialTheme.colorScheme.onBackground
            ),
            singleLine = true
        )

        Spinner(
            list = Event.Type.values().map { it.toString() },
            selected = uiState.type.toString(),
            onSelectionChanged = { onEventTypeChange(Event.Type.valueOf(it)) }
        )

        // Start date & time picker
        Row {
            val startDate = uiState.start - uiState.start % MILLISECONDS_IN_DAY
            val startHour = uiState.start.getHour()
            val startMinute = uiState.start.getMinute()

            var showStartDatePicker by remember { mutableStateOf(false) }
            var showStartTimePicker by remember { mutableStateOf(false) }

            // Start date picker
            Box(contentAlignment = Alignment.Center) {
                Button(onClick = { showStartDatePicker = true }) {
                    Text(text = startDate.getReadableDate())
                }
            }

            if (showStartDatePicker) {
                MyDatePickerDialog(
                    onDateSelected = { newDateMillis ->
                        onStartDateChange(newDateMillis + startHour * MILLISECONDS_IN_HOUR + startMinute * MILLISECONDS_IN_MINUTE)
                    },
                    onDismiss = { showStartDatePicker = false }
                )
            }

            // Start time picker
            Box(contentAlignment = Alignment.Center) {
                Button(onClick = { showStartTimePicker = true }) {
                    Text(text = "$startHour:$startMinute")
                }
            }

            if (showStartTimePicker) {
                MyTimePickerDialog(
                    onTimeSelected = { newHour, newMinute ->
                        onStartDateChange(startDate + newHour * MILLISECONDS_IN_HOUR + newMinute * MILLISECONDS_IN_MINUTE)
                    },
                    onDismiss = { showStartTimePicker = false }
                )
            }
        }

        // End date & time picker
        Row {
            val endDate = uiState.end - uiState.end % MILLISECONDS_IN_DAY
            val endHour = uiState.end.getHour()
            val endMinute = uiState.end.getMinute()

            var showEndDatePicker by remember { mutableStateOf(false) }
            var showEndTimePicker by remember { mutableStateOf(false) }

            // End date picker
            Box(contentAlignment = Alignment.Center) {
                Button(onClick = { showEndDatePicker = true }) {
                    Text(text = endDate.getReadableDate())
                }
            }

            if (showEndDatePicker) {
                MyDatePickerDialog(
                    onDateSelected = { newDateMillis ->
                        onEndDateChange(newDateMillis + endHour * MILLISECONDS_IN_HOUR + endMinute * MILLISECONDS_IN_MINUTE)
                    },
                    onDismiss = { showEndDatePicker = false }
                )
            }

            // End time picker
            Box(contentAlignment = Alignment.Center) {
                Button(onClick = { showEndTimePicker = true }) {
                    Text(text = "$endHour:$endMinute")
                }
            }

            if (showEndTimePicker) {
                MyTimePickerDialog(
                    onTimeSelected = { newHour, newMinute ->
                        onEndDateChange(endDate + newHour * MILLISECONDS_IN_HOUR + newMinute * MILLISECONDS_IN_MINUTE)
                    },
                    onDismiss = { showEndTimePicker = false }
                )
            }
        }

        Button(onClick = onSubmitEventButtonClick) {
            Text(stringResource(R.string.submit_event))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun NewEventScreenPreview() {
    PrepPalTheme {
        NewEventScreen(
            uiState = NewEventScreenUiState(
                summary = "Комп'ютерні системи",
                type = Event.Type.Lecture,
                start = System.currentTimeMillis(),
                end = System.currentTimeMillis()
            )
        )
    }
}