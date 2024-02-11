package the_null_pointer.preppal.ui.set_grade

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import the_null_pointer.preppal.R
import the_null_pointer.preppal.data.event.model.Event
import the_null_pointer.preppal.data.event.model.Event.Type.Companion.completionStringResourceId
import the_null_pointer.preppal.data.event.model.Event.Type.Companion.stringResourceId
import the_null_pointer.preppal.ui.widget.CheckboxWithoutPadding
import the_null_pointer.preppal.ui.widget.MapView
import the_null_pointer.preppal.util.OpenStreetMapUtil.addMarker
import the_null_pointer.preppal.util.OpenStreetMapUtil.clearAllMarkers
import the_null_pointer.preppal.util.OpenStreetMapUtil.toBoundingBox
import the_null_pointer.preppal.util.TimeUtil.getReadableTimePeriod

@SuppressLint("ClickableViewAccessibility")
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
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_left),
                contentDescription = stringResource(R.string.go_back),
                modifier = Modifier
                    .defaultMinSize(48.dp, 48.dp)
                    .clickable { onBackClicked() }
                    .align(Alignment.CenterStart)
            )

            Text(
                text = stringResource(uiState.event.type.stringResourceId),
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(6.dp)
                    .fillMaxWidth()
                    .align(Alignment.Center)
            )
        }

        SimpleText(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(6.dp),
            text = uiState.event.summary
        )
        SimpleText(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(6.dp),
            text = uiState.event.start.getReadableTimePeriod(uiState.event.end)
        )
        if (uiState.event.locationName != null) {
            SimpleText(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp),
                text = uiState.event.locationName
            )
        }

        // Display current and max grades
        if (uiState.event.graded) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                TextField(
                    label = { Text(stringResource(R.string.get_grade)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.5f)
                        .padding(start = 8.dp, end = 8.dp),
                    value = (uiState.event.grade ?: 0.0).toString(),
                    onValueChange = onCurrentGradeValueChange,
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done

                    )
                )

                TextField(
                    label = { Text(stringResource(R.string.max_grade)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.5f)
                        .padding(start = 8.dp, end = 8.dp),
                    value = (uiState.event.maxGrade ?: 0.0).toString(),
                    onValueChange = onMaxGradeValueChange,
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    )
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

        if (uiState.event.location != null) {
            Spacer(Modifier.height(8.dp))
            val context = LocalContext.current

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(horizontal = 6.dp)
            ) {
                Card(
                    modifier = Modifier
                        .height(200.dp)
                ) {
                    MapView { mapView ->
                        Configuration.getInstance().userAgentValue =
                            mapView.context.getString(R.string.app_name)
                        mapView.setOnTouchListener { _, _ ->
                            val gmmIntentUri =
                                Uri.parse("geo:${uiState.event.location.latitude},${uiState.event.location.longitude}" +
                                        "?q=${uiState.event.location.latitude},${uiState.event.location.longitude}")
                            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                            mapIntent.setPackage("com.google.android.apps.maps")
                            if (mapIntent.resolveActivity(context.packageManager) != null) {
                                startActivity(context, mapIntent, Bundle.EMPTY)
                            }

                            true
                        }
                        mapView.zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
                        mapView.isTilesScaledToDpi = true

                        mapView.overlayManager.tilesOverlay.tileStates.runAfters.add {
                            if (mapView.zoomLevelDouble == mapView.minZoomLevel) {
                                mapView.zoomToBoundingBox(
                                    GeoPoint(
                                        uiState.event.location.latitude,
                                        uiState.event.location.longitude
                                    ).toBoundingBox(),
                                    false
                                )

                                mapView.clearAllMarkers()
                                mapView.addMarker(
                                    latitude = uiState.event.location.latitude,
                                    longitude = uiState.event.location.longitude,
                                    text = "${
                                        uiState.event.location.latitude.toString().take(6)
                                    }, " +
                                            uiState.event.location.longitude.toString().take(6)
                                )
                                mapView.controller.animateTo(
                                    GeoPoint(
                                        uiState.event.location.latitude,
                                        uiState.event.location.longitude
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun SimpleText(
    text: String,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        modifier = modifier.clickable { },
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline
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
