package the_null_pointer.preppal.ui.new_event

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BasicAlertDialog
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import the_null_pointer.preppal.R
import the_null_pointer.preppal.data.Event
import the_null_pointer.preppal.data.Event.RecurrenceType.Companion.stringResourceId
import the_null_pointer.preppal.data.Event.Type.Companion.stringResourceId
import the_null_pointer.preppal.data.TimestampMillis
import the_null_pointer.preppal.ui.theme.PrepPalTheme
import the_null_pointer.preppal.ui.widget.MapView
import the_null_pointer.preppal.ui.widget.MultiChoiceSpinner
import the_null_pointer.preppal.ui.widget.MyDatePickerDialog
import the_null_pointer.preppal.ui.widget.MyTimePickerDialog
import the_null_pointer.preppal.ui.widget.Spinner
import the_null_pointer.preppal.util.OpenStreetMapUtil.addMarker
import the_null_pointer.preppal.util.OpenStreetMapUtil.clearAllMarkers
import the_null_pointer.preppal.util.OpenStreetMapUtil.toBoundingBox
import the_null_pointer.preppal.util.OpenStreetMapUtil.ukraineBoundingBox
import the_null_pointer.preppal.util.TimeUtil.MILLISECONDS_IN_DAY
import the_null_pointer.preppal.util.TimeUtil.MILLISECONDS_IN_HOUR
import the_null_pointer.preppal.util.TimeUtil.MILLISECONDS_IN_MINUTE
import the_null_pointer.preppal.util.TimeUtil.getHour
import the_null_pointer.preppal.util.TimeUtil.getMinute
import the_null_pointer.preppal.util.TimeUtil.getReadableDate

// За 5 хв, за годину, за день, за тиждень
private val reminders = listOf(
    (-5 * MILLISECONDS_IN_MINUTE) to R.string.reminder_5_minutes,
    (-1 * MILLISECONDS_IN_HOUR) to R.string.reminder_1_hour,
    (-1 * MILLISECONDS_IN_DAY) to R.string.reminder_1_day,
    (-7 * MILLISECONDS_IN_DAY) to R.string.reminder_1_week,
)

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
    onSubmitEventButtonClick: () -> Unit = {}
) {
    var locationConfirmed by rememberSaveable { mutableStateOf(false) }
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
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Start/end date & time picker
            val startDate = uiState.start - uiState.start % MILLISECONDS_IN_DAY
            val startHour = uiState.start.getHour()
            val startMinute = uiState.start.getMinute()

            val endDate = uiState.end - uiState.end % MILLISECONDS_IN_DAY
            val endHour = uiState.end.getHour()
            val endMinute = uiState.end.getMinute()

            var showDatePicker by remember { mutableStateOf(false) }
            var showStartTimePicker by remember { mutableStateOf(false) }
            var showEndTimePicker by remember { mutableStateOf(false) }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
            ) {
                Column(
                    modifier = Modifier
                        .weight(0.2f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(text = stringResource(R.string.from))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = stringResource(R.string.to))
                }

                Row(
                    modifier = Modifier
                        .weight(0.8f),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        // Date picker
                        Box(contentAlignment = Alignment.Center) {
                            Button(
                                modifier = Modifier.fillMaxHeight(),
                                onClick = { showDatePicker = true }
                            ) {
                                Text(text = startDate.getReadableDate())
                            }
                        }

                        if (showDatePicker) {
                            MyDatePickerDialog(
                                initialSelectedDateMillis = startDate,
                                onDateSelected = { newDateMillis ->
                                    onStartDateChange(newDateMillis + startHour * MILLISECONDS_IN_HOUR + startMinute * MILLISECONDS_IN_MINUTE)
                                    onEndDateChange(newDateMillis + endHour * MILLISECONDS_IN_HOUR + startMinute * MILLISECONDS_IN_MINUTE)
                                },
                                onDismiss = { showDatePicker = false }
                            )
                        }
                    }

                    Column {
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

                        Spacer(Modifier.height(4.dp))

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

            Spacer(modifier = Modifier.height(4.dp))

            if (uiState.recurrenceType != null) {
                // Recurrence End date picker
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.weight(0.5f),
                        text = stringResource(R.string.repeat_until)
                    )

                    Row(modifier = Modifier.weight(0.5f), horizontalArrangement = Arrangement.End) {
                        val endRecurrenceDate =
                            uiState.recurrenceEndDate - uiState.recurrenceEndDate % MILLISECONDS_IN_DAY

                        var showEndRecurrenceDatePicker by remember { mutableStateOf(false) }

                        Box(contentAlignment = Alignment.Center) {
                            Button(onClick = { showEndRecurrenceDatePicker = true }) {
                                Text(text = endRecurrenceDate.getReadableDate())
                            }
                        }

                        if (showEndRecurrenceDatePicker) {
                            MyDatePickerDialog(
                                initialSelectedDateMillis = endRecurrenceDate,
                                onDateSelected = { newDateMillis ->
                                    onRecurrenceEndDateChange(newDateMillis)
                                },
                                onDismiss = { showEndRecurrenceDatePicker = false },
                                isSelectableDate = { utcTimeMillis ->
                                    utcTimeMillis >= uiState.start
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Reminders
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Reminder Checkbox
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = uiState.isReminderEnabled,
                        onCheckedChange = onReminderStateChange
                    )

                    Text(text = stringResource(R.string.reminder))
                }

                Spacer(Modifier.width(16.dp))

                // Reminders multi-choice Spinner
                if (uiState.isReminderEnabled) {
                    MultiChoiceSpinner(
                        modifier = Modifier.fillMaxWidth(),
                        items = reminders.map { it.first to stringResource(it.second) },
                        selected = uiState.reminderOffsets,
                        onSelectionChanged = onReminderOffsetsChange
                    )
                }
            }

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
                    onCheckedChange = { checked ->
                        // To let user re-choose location even after confirmation
                        if (!checked && locationConfirmed) locationConfirmed = false
                        onLocationStateChange(checked)
                    }
                )

                Text(text = stringResource(R.string.add_event_location))
            }

            // Location preview on MapView
            if (uiState.isLocationEnabled && uiState.locationLatitude != null
                && uiState.locationLongitude != null && locationConfirmed
            ) {
                Card(Modifier.size(200.dp)) {
                    MapView { mapView ->
                        Configuration.getInstance().userAgentValue =
                            mapView.context.getString(R.string.app_name)
                        mapView.setOnTouchListener { _, _ -> true }
                        mapView.zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
                        mapView.isTilesScaledToDpi = true

                        mapView.overlayManager.tilesOverlay.tileStates.runAfters.add {
                            if (mapView.zoomLevelDouble == mapView.minZoomLevel) {
                                mapView.zoomToBoundingBox(
                                    GeoPoint(
                                        uiState.locationLatitude,
                                        uiState.locationLongitude
                                    ).toBoundingBox(),
                                    false
                                )

                                mapView.clearAllMarkers()
                                mapView.addMarker(
                                    latitude = uiState.locationLatitude,
                                    longitude = uiState.locationLongitude,
                                    text = "${
                                        uiState.locationLatitude.toString().take(6)
                                    }, " +
                                            uiState.locationLongitude.toString().take(6)
                                )
                                mapView.controller.animateTo(
                                    GeoPoint(
                                        uiState.locationLatitude,
                                        uiState.locationLongitude
                                    )
                                )
                            }
                        }
                    }
                }
            }

            Button(onClick = onSubmitEventButtonClick) {
                Text(stringResource(R.string.submit_event))
            }
        }

        // Location MapView dialog
        if (uiState.isLocationEnabled && !locationConfirmed) {
            mapEventsReceiver.onLocationChange = onLocationChange
            BasicAlertDialog(
                onDismissRequest = { onLocationStateChange(false) }
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Card(Modifier.size(300.dp)) {
                        MapView { mapView ->
                            mapEventsReceiver.mapView = mapView

                            Configuration.getInstance().userAgentValue =
                                mapView.context.getString(R.string.app_name)
                            mapView.setMultiTouchControls(true)
                            mapView.isTilesScaledToDpi = true

                            mapView.overlayManager.tilesOverlay.tileStates.runAfters.add {
                                if (mapView.zoomLevelDouble == mapView.minZoomLevel) {
                                    mapView.zoomToBoundingBox(ukraineBoundingBox, false)

                                    if (uiState.locationLatitude != null && uiState.locationLongitude != null && !locationConfirmed) {
                                        mapView.zoomToBoundingBox(
                                            GeoPoint(
                                                uiState.locationLatitude,
                                                uiState.locationLongitude
                                            ).toBoundingBox(diff = 0.005),
                                            false
                                        )
                                        mapView.clearAllMarkers()
                                        mapView.addMarker(
                                            latitude = uiState.locationLatitude,
                                            longitude = uiState.locationLongitude,
                                            text = "${
                                                uiState.locationLatitude.toString().take(6)
                                            }, " + uiState.locationLongitude.toString().take(6)
                                        )
                                    }
                                }
                            }
                            if (!mapView.overlays.contains(mapEventsOverlay)) {
                                mapView.overlays.add(mapEventsOverlay)
                            }
                        }
                    }

                    Card(
                        modifier = Modifier
                            .padding(4.dp)
                            .shadow(4.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            if (uiState.locationLatitude != null && uiState.locationLongitude != null) {
                                Text(
                                    text = "${uiState.locationLatitude.toString().take(6)}, " +
                                            uiState.locationLongitude.toString().take(6)
                                )
                            }

                            Button(onClick = { locationConfirmed = true }) {
                                Text(stringResource(R.string.confirm_location))
                            }
                        }
                    }
                }
            }
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