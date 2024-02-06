package the_null_pointer.preppal.ui.new_event

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import the_null_pointer.preppal.R
import the_null_pointer.preppal.data.Event
import the_null_pointer.preppal.data.Event.RecurrenceType.Companion.stringResourceId
import the_null_pointer.preppal.data.Event.Type.Companion.stringResourceId
import the_null_pointer.preppal.ui.theme.PrepPalTheme
import the_null_pointer.preppal.ui.widget.MapView
import the_null_pointer.preppal.ui.widget.MyDatePickerDialog
import the_null_pointer.preppal.ui.widget.MyTimePickerDialog
import the_null_pointer.preppal.ui.widget.Spinner
import the_null_pointer.preppal.util.OpenStreetMapUtil.addMarker
import the_null_pointer.preppal.util.OpenStreetMapUtil.clearAllMarkers
import the_null_pointer.preppal.util.OpenStreetMapUtil.ukraineBoundingBox
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
    onEventRecurrenceTypeChange: (Event.RecurrenceType?) -> Unit = {},
    onStartDateChange: (Long) -> Unit = {},
    onEndDateChange: (Long) -> Unit = {},
    onReminderStateChange: (Boolean) -> Unit = {},
    onLocationStateChange: (Boolean) -> Unit = {},
    onLocationChange: (Double, Double) -> Unit = { _, _ -> },
    onGradedChange: (Boolean) -> Unit = {},
    onSubmitEventButtonClick: () -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(4.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Event Type
            Spinner(
                items = Event.Type.values().map { it to stringResource(it.stringResourceId) },
                selected = uiState.type,
                onSelectionChanged = { it?.let(onEventTypeChange) }
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Event Summary
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(stringResource(R.string.event_summary_placeholder)) },
                value = uiState.summary,
                onValueChange = onSummaryValueChange,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Start date & time picker
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(modifier = Modifier.weight(0.2f), text = stringResource(R.string.from))

                Row(modifier = Modifier.weight(0.8f), horizontalArrangement = Arrangement.End) {

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
                            Text(
                                text = "${
                                    startHour.toString().padStart(2, '0')
                                }:${startMinute.toString().padStart(2, '0')}"
                            )
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
            }

            Spacer(modifier = Modifier.height(4.dp))

            // End date & time picker
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(modifier = Modifier.weight(0.2f), text = stringResource(R.string.to))

                Row(modifier = Modifier.weight(0.8f), horizontalArrangement = Arrangement.End) {
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
                            Text(
                                text = "${endHour.toString().padStart(2, '0')}:${
                                    endMinute.toString().padStart(2, '0')
                                }"
                            )
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
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Event Recurrence Type
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(modifier = Modifier.weight(0.3f), text = stringResource(R.string.recurrence))

                Spinner(
                    modifier = Modifier.weight(0.7f),
                    items = Event.RecurrenceType.values()
                        .map { it to stringResource(it.stringResourceId) },
                    selected = uiState.recurrenceType,
                    onSelectionChanged = { onEventRecurrenceTypeChange(it) },
                    canSelectNothing = true,
                    nothingOptionString = stringResource(R.string.never)
                )
            }

            // TODO: Recurrence End date and time picker ("Закінчення повтору")

            Spacer(modifier = Modifier.height(4.dp))

            // Reminder Checkbox
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = uiState.isReminderEnabled,
                    onCheckedChange = onReminderStateChange
                )

                Text(text = stringResource(R.string.reminder))
            }

            // TODO: Reminder Spinner

            Spacer(modifier = Modifier.height(4.dp))

            // Graded Checkbox
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = uiState.isGraded,
                    onCheckedChange = onGradedChange
                )

                Text(text = stringResource(R.string.is_graded))
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Location Checkbox
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = uiState.isLocationEnabled,
                    onCheckedChange = onLocationStateChange
                )

                Text(text = stringResource(R.string.add_event_location))
            }
        }

        // Location MapView
        if (uiState.isLocationEnabled) {
            mapEventsReceiver.onLocationChange = onLocationChange
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            ) {
                MapView { mapView ->
                    mapEventsReceiver.mapView = mapView

                    Configuration.getInstance().userAgentValue =
                        mapView.context.getString(R.string.app_name)
                    mapView.setMultiTouchControls(true)
                    mapView.isTilesScaledToDpi = true

                    mapView.overlayManager.tilesOverlay.tileStates.runAfters.add {
                        if (mapView.zoomLevelDouble == mapView.minZoomLevel) {
                            mapView.zoomToBoundingBox(ukraineBoundingBox, false)

                            if (uiState.locationLatitude != null && uiState.locationLongitude != null) {
                                mapView.clearAllMarkers()
                                mapView.addMarker(
                                    latitude = uiState.locationLatitude,
                                    longitude = uiState.locationLongitude,
                                    text = "${uiState.locationLatitude.toString().take(6)}, " +
                                            uiState.locationLongitude.toString().take(6)
                                )
                            }
                        }
                    }
                    if (!mapView.overlays.contains(mapEventsOverlay)) {
                        mapView.overlays.add(mapEventsOverlay)
                    }
                }
            }
        }

        Button(onClick = onSubmitEventButtonClick) {
            Text(stringResource(R.string.submit_event))
        }
    }
}

private val mapEventsReceiver = object : MapEventsReceiver {
    lateinit var mapView: MapView
    lateinit var onLocationChange: (Double, Double) -> Unit

    override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
        mapView.clearAllMarkers()
        p?.let {
            mapView.addMarker(
                latitude = p.latitude,
                longitude = p.longitude,
                text = "${p.latitude.toString().take(6)}, ${p.longitude.toString().take(6)}"
            )
            onLocationChange(p.latitude, p.longitude)
        }
        return true
    }

    override fun longPressHelper(p: GeoPoint?): Boolean = false
}

private val mapEventsOverlay = MapEventsOverlay(mapEventsReceiver)

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