package the_null_pointer.preppal.ui.event.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import the_null_pointer.preppal.R
import the_null_pointer.preppal.data.Event
import the_null_pointer.preppal.data.TimestampMillis
import the_null_pointer.preppal.ui.event.BaseEventScreen
import the_null_pointer.preppal.ui.event.BaseEventScreenUiState
import the_null_pointer.preppal.ui.widget.Spinner

@Composable
fun EditEventScreen(
    uiState: BaseEventScreenUiState,
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
    onDeleteEventButtonClick: (EventDeleteMethod) -> Unit = {}
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
            defaultLocationConfirmState = true
        )

        if (uiState.recurrenceType == null) {
            Button(onClick = { onDeleteEventButtonClick(EventDeleteMethod.DeleteOne) }) {
                Text(stringResource(R.string.delete))
            }
        } else {
            Spinner(
                modifier = Modifier.padding(4.dp),
                cardColors = CardDefaults.outlinedCardColors()
                    .copy(containerColor = MaterialTheme.colorScheme.errorContainer),
                items = listOf(
                    EventDeleteMethod.DeleteOne to stringResource(R.string.delete_one_event),
                    EventDeleteMethod.DeleteOneAndFollowing to stringResource(R.string.delete_one_and_following_events),
                    EventDeleteMethod.DeleteAll to stringResource(R.string.delete_all_events)
                ),
                selected = null,
                onSelectionChanged = {
                    if (it != null) {
                        onDeleteEventButtonClick(it)
                    }
                },
                canSelectNothing = true,
                nothingOptionString = stringResource(R.string.deletion)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditEventScreenPreview() {
    EditEventScreen(uiState = EditEventScreenUiState())
}