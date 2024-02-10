package the_null_pointer.preppal.ui.event.add_new

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import the_null_pointer.preppal.R
import the_null_pointer.preppal.data.event.model.Event
import the_null_pointer.preppal.data.event.model.TimestampMillis
import the_null_pointer.preppal.ui.event.BaseEventScreen
import the_null_pointer.preppal.ui.theme.PrepPalTheme

@ExperimentalMaterial3Api
@Composable
fun NewEventScreen(
    uiState: NewEventScreenUiState,
    onSummaryValueChange: (String) -> Unit = {},
    onEventTypeChange: (Event.Type) -> Unit = {},
    onEventRecurrenceTypeChange: (Event.RecurrenceType?) -> Unit = {},
    onRecurrenceEndDateChange: (Long) -> Unit = {},
    onStartDateChange: (Long) -> Unit = {},
    onEndDateChange: (Long) -> Unit = {},
    onReminderStateChange: (Boolean) -> Unit = {},
    onReminderOffsetsChange: (List<TimestampMillis>) -> Unit = {},
    onLocationStateChange: (Boolean) -> Unit = {},
    onLocationChange: (Double, Double) -> Unit = { _, _ -> },
    onGradedChange: (Boolean) -> Unit = {},
    onSubmitEventButtonClick: () -> Unit = {},
    defaultLocationConfirmState: Boolean = false
) {
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.SpaceBetween
    ) {
        BaseEventScreen(
            uiState,
            onSummaryValueChange,
            onEventTypeChange,
            onEventRecurrenceTypeChange,
            onRecurrenceEndDateChange,
            onStartDateChange,
            onEndDateChange,
            onReminderStateChange,
            onReminderOffsetsChange,
            onLocationStateChange,
            onLocationChange,
            onGradedChange,
            defaultLocationConfirmState
        )

        Button(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth(),
            onClick = onSubmitEventButtonClick
        ) {
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