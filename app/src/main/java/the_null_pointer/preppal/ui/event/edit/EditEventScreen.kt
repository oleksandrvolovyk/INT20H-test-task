package the_null_pointer.preppal.ui.event.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import the_null_pointer.preppal.ui.widget.Spinner

@Composable
fun EditEventScreen(
    uiState: EditEventScreenUiState,
    onSummaryValueChange: (String) -> Unit = {},
    onEventTypeChange: (Event.Type) -> Unit = {},
    onStartDateChange: (Long) -> Unit = {},
    onEndDateChange: (Long) -> Unit = {},
    onReminderStateChange: (Boolean) -> Unit = {},
    onReminderOffsetsChange: (List<TimestampMillis>) -> Unit = {},
    onLocationStateChange: (Boolean) -> Unit = {},
    onLocationChange: (Double, Double) -> Unit = { _, _ -> },
    onGradedChange: (Boolean) -> Unit = {},
    onSubmitChangesButtonClick: (EventEditMethod) -> Unit = {},
    onDeleteEventButtonClick: (EventDeleteMethod) -> Unit = {}
) {
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.SpaceBetween
    ) {
        BaseEventScreen(
            uiState = uiState,
            onSummaryValueChange = onSummaryValueChange,
            onEventTypeChange = onEventTypeChange,
            onStartDateChange = onStartDateChange,
            onEndDateChange = onEndDateChange,
            onReminderStateChange = onReminderStateChange,
            onReminderOffsetsChange = onReminderOffsetsChange,
            onLocationStateChange = onLocationStateChange,
            onLocationChange = onLocationChange,
            onGradedChange = onGradedChange,
            defaultLocationConfirmationState = true,
            editMode = true
        )

        Column {
            if (EventEditMethod.EditOne in uiState.availableEditMethods) {
                Button(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .fillMaxWidth(),
                    onClick = { onSubmitChangesButtonClick(EventEditMethod.EditOne) }
                ) {
                    Text(stringResource(R.string.save_changes_to_one_event))
                }
            }

            if (EventEditMethod.EditAll in uiState.availableEditMethods) {
                Button(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .fillMaxWidth(),
                    onClick = { onSubmitChangesButtonClick(EventEditMethod.EditAll) }
                ) {
                    Text(stringResource(R.string.save_changes_to_all_events))
                }
            }

            if (uiState.recurrenceType == null) {
                Button(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .fillMaxWidth(),
                    onClick = {
                        onDeleteEventButtonClick(
                            EventDeleteMethod.DeleteOne
                        )
                    },
                    colors = ButtonDefaults.buttonColors()
                        .copy(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                        )
                ) {
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
}

@Preview(showBackground = true)
@Composable
fun EditEventScreenPreview() {
    EditEventScreen(uiState = EditEventScreenUiState())
}