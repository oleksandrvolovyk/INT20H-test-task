package the_null_pointer.preppal.ui.grades.by_summary_and_type

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import the_null_pointer.preppal.R
import the_null_pointer.preppal.data.event.model.Event
import the_null_pointer.preppal.data.event.model.Event.Type.Companion.stringResourceId

@Composable
fun GradesBySummaryAndTypeScreen(
    uiState: GradesBySummaryAndTypeScreenUiState,
    onEventClick: (eventId: Long) -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_left),
                contentDescription = stringResource(R.string.go_back),
                modifier = Modifier
                    .defaultMinSize(48.dp, 48.dp)
                    .clickable { onBackClick() }
                    .align(Alignment.CenterStart)
            )

            Text(
                text = stringResource(
                    R.string.grades_of_summary_and_type,
                    uiState.eventSummary,
                    stringResource(uiState.eventType.stringResourceId)
                ),
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(6.dp)
                    .fillMaxWidth()
                    .align(Alignment.Center)
            )
        }

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(uiState.progressBySummaryAndTypeListItems, key = { it.time }) {
                ProgressRow(
                    eventSummary = uiState.eventSummary,
                    eventType = uiState.eventType,
                    gradesBySummaryAndTypeListItem = it,
                    onEventClick = onEventClick
                )
            }
        }
    }
}

@Composable
fun ProgressRow(
    eventSummary: String,
    eventType: Event.Type,
    gradesBySummaryAndTypeListItem: GradesBySummaryAndTypeListItem,
    onEventClick: (eventId: Long) -> Unit
) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .clip(shape = RoundedCornerShape(4.dp))
            .padding(6.dp),
        border = BorderStroke(
            width = 3.dp,
            color = MaterialTheme.colorScheme.outline
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .clickable { onEventClick(gradesBySummaryAndTypeListItem.eventId) },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "${stringResource(id = eventType.stringResourceId)}\n${eventSummary}",
                modifier = Modifier
                    .padding(4.dp)
                    .weight(1.5f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = gradesBySummaryAndTypeListItem.time,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(4.dp)
                    .weight(0.9f)
            )

            Text(
                text = stringResource(
                    R.string.grade,
                    gradesBySummaryAndTypeListItem.grade ?: "-",
                    gradesBySummaryAndTypeListItem.maxGrade ?: "-"
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(4.dp)
                    .weight(0.9f)
            )
        }
    }
}