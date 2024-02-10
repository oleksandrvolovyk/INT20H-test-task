package the_null_pointer.preppal.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.OutDateStyle
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import kotlinx.coroutines.launch
import the_null_pointer.preppal.R
import the_null_pointer.preppal.data.event.model.Event
import the_null_pointer.preppal.data.event.model.Event.Type.Companion.completionStringResourceId
import the_null_pointer.preppal.data.event.model.Event.Type.Companion.stringResourceId
import the_null_pointer.preppal.ui.theme.PrepPalTheme
import the_null_pointer.preppal.ui.widget.CheckboxWithoutPadding
import the_null_pointer.preppal.util.TimeUtil.getHourAsString
import the_null_pointer.preppal.util.TimeUtil.getMinuteAsString
import java.time.DayOfWeek
import java.time.YearMonth

private val pageBackgroundColor: Color @Composable get() = MaterialTheme.colorScheme.background
private val itemBackgroundColor: Color @Composable get() = MaterialTheme.colorScheme.tertiaryContainer
private val toolbarColor: Color @Composable get() = MaterialTheme.colorScheme.tertiaryContainer
private val selectedItemBorderColor: Color @Composable get() = MaterialTheme.colorScheme.secondary
private val inActiveTextColor: Color @Composable get() = MaterialTheme.colorScheme.onTertiaryContainer

@Composable
fun CalendarScreen(
    uiState: CalendarScreenUiState,
    onNewEventButtonClick: (selectedEpochDay: Long?) -> Unit = {},
    onEventClick: (eventId: Long) -> Unit = {},
    onEventCompletionChange: (eventId: Long, Boolean) -> Unit = { _, _ -> },
    onEventGradeClick: (eventId: Long) -> Unit = {}
) {
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(500) }
    val endMonth = remember { currentMonth.plusMonths(500) }
    var selection by remember { mutableStateOf<CalendarDay?>(null) }
    val daysOfWeek = remember { daysOfWeek() }
    var eventsInSelectedDate by remember { mutableStateOf<List<Event>>(emptyList()) }

    LaunchedEffect(selection, uiState.events) {
        val date = selection?.date
        eventsInSelectedDate = if (date == null) emptyList() else uiState.events[date].orEmpty()
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onNewEventButtonClick(selection?.date?.toEpochDay()) },
                modifier = Modifier.padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.add_new_event)
                )
            }
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
                .background(pageBackgroundColor)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.End
        ) {
            val state = rememberCalendarState(
                startMonth = startMonth,
                endMonth = endMonth,
                firstVisibleMonth = currentMonth,
                firstDayOfWeek = daysOfWeek.first(),
                outDateStyle = OutDateStyle.EndOfGrid,
            )
            val coroutineScope = rememberCoroutineScope()
            val visibleMonth = rememberFirstCompletelyVisibleMonth(state)
            LaunchedEffect(visibleMonth) {
                // Clear selection if we scroll to a new month.
                selection = null
            }

            // Draw light content on dark background.
            CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurface) {
                SimpleCalendarTitle(
                    modifier = Modifier
                        .background(toolbarColor)
                        .padding(horizontal = 8.dp, vertical = 12.dp),
                    currentMonth = visibleMonth.yearMonth,
                    goToPrevious = {
                        coroutineScope.launch {
                            state.animateScrollToMonth(state.firstVisibleMonth.yearMonth.previousMonth)
                        }
                    },
                    goToNext = {
                        coroutineScope.launch {
                            state.animateScrollToMonth(state.firstVisibleMonth.yearMonth.nextMonth)
                        }
                    },
                )
                HorizontalCalendar(
                    modifier = Modifier.wrapContentWidth(),
                    state = state,
                    dayContent = { day ->
                        CompositionLocalProvider(LocalRippleTheme provides CalendarRippleTheme) {
                            val colors = if (day.position == DayPosition.MonthDate) {
                                uiState.events[day.date].orEmpty().map { MaterialTheme.colorScheme.onTertiaryContainer }
                            } else {
                                emptyList()
                            }
                            Day(
                                day = day,
                                isSelected = selection == day,
                                colors = colors,
                            ) { clicked ->
                                selection = clicked
                            }
                        }
                    },
                    monthHeader = {
                        MonthHeader(
                            modifier = Modifier.padding(vertical = 8.dp),
                            daysOfWeek = daysOfWeek,
                        )
                    },
                )
                Divider(color = pageBackgroundColor)
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                ) {
                    items(items = eventsInSelectedDate) { event ->
                        EventInformation(
                            modifier = Modifier.clickable { onEventClick(event.id) },
                            event = event,
                            onEventCompletionChange = onEventCompletionChange,
                            onEventGradeClick = onEventGradeClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Day(
    day: CalendarDay,
    isSelected: Boolean = false,
    colors: List<Color> = emptyList(),
    onClick: (CalendarDay) -> Unit = {},
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f) // This is important for square-sizing!
            .border(
                width = if (isSelected) 1.dp else 0.dp,
                color = if (isSelected) selectedItemBorderColor else Color.Transparent,
            )
            .padding(1.dp)
            .background(color = itemBackgroundColor)
            // Disable clicks on inDates/outDates
            .clickable(
                enabled = day.position == DayPosition.MonthDate,
                onClick = { onClick(day) },
            ),
    ) {
        val textColor = when (day.position) {
            DayPosition.MonthDate -> Color.Unspecified
            DayPosition.InDate, DayPosition.OutDate -> inActiveTextColor
        }
        Text(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 3.dp, end = 4.dp),
            text = day.date.dayOfMonth.toString(),
            color = textColor,
            fontSize = 12.sp,
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            for (color in colors) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(5.dp)
                        .background(color),
                )
            }
        }
    }
}

@Composable
private fun MonthHeader(
    modifier: Modifier = Modifier,
    daysOfWeek: List<DayOfWeek> = emptyList(),
) {
    Row(modifier.fillMaxWidth()) {
        for (dayOfWeek in daysOfWeek) {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                text = dayOfWeek.displayText(uppercase = true),
                fontWeight = FontWeight.Light,
            )
        }
    }
}

@Composable
private fun LazyItemScope.EventInformation(
    event: Event,
    modifier: Modifier = Modifier,
    onEventCompletionChange: (eventId: Long, Boolean) -> Unit,
    onEventGradeClick: (eventId: Long) -> Unit
) {
    Row(
        modifier = modifier
            .fillParentMaxWidth()
            .height(IntrinsicSize.Max),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Box(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.primaryContainer)
                .fillParentMaxWidth(1 / 7f)
                .aspectRatio(1f),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "${event.start.getHourAsString()}:${event.start.getMinuteAsString()}\n" +
                        "${event.end.getHourAsString()}:${event.end.getMinuteAsString()}",
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textAlign = TextAlign.Center,
                lineHeight = 17.sp,
                fontSize = 12.sp,
            )
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
        ) {
            EventDetails(event = event, onEventCompletionChange, onEventGradeClick)
        }
    }
    Divider(thickness = 2.dp)
}

@Composable
private fun EventDetails(
    event: Event,
    onEventCompletionChange: (eventId: Long, Boolean) -> Unit,
    onEventGradeClick: (eventId: Long) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .weight(0.7f)
                    .fillMaxHeight()
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(event.type.stringResourceId),
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = event.summary,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Light,
                )
            }
            Row(
                modifier = Modifier.fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (event.completed != null) {
                    Column(
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .fillMaxHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = stringResource(event.type.completionStringResourceId))

                        CheckboxWithoutPadding(
                            checked = event.completed,
                            onCheckedChange = { onEventCompletionChange(event.id, it) }
                        )
                    }
                }

                if (event.graded) {
                    Column(
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxHeight()
                            .clickable { onEventGradeClick(event.id) },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            textAlign = TextAlign.Center,
                            text = stringResource(
                                R.string.grade,
                                event.grade?.toString() ?: "-",
                                event.maxGrade?.toString() ?: "-"
                            )
                        )
                    }
                }
            }
        }
    }
}

// The default dark them ripple is too bright so we tone it down.
private object CalendarRippleTheme : RippleTheme {
    @Composable
    override fun defaultColor() = RippleTheme.defaultRippleColor(Color.Gray, lightTheme = false)

    @Composable
    override fun rippleAlpha() = RippleTheme.defaultRippleAlpha(Color.Gray, lightTheme = false)
}

@Preview(showBackground = true)
@Composable
fun CalendarPreview() {
    PrepPalTheme {
        CalendarScreen(
            uiState = CalendarScreenUiState(

            )
        )
    }
}
