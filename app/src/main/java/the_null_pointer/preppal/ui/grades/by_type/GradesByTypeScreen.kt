package the_null_pointer.preppal.ui.grades.by_type

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import the_null_pointer.preppal.R
import the_null_pointer.preppal.data.event.model.Event
import the_null_pointer.preppal.data.event.model.Event.Type.Companion.stringResourceId
import the_null_pointer.preppal.ui.theme.PrepPalTheme

@Composable
fun GradesByTypeScreen(
    uiState: GradesByTypeScreenUiState,
    onSummaryAndTypeClick: (summary: String, type: Event.Type) -> Unit = { _, _ -> },
    onBackClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState())
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
                    R.string.scoreboard_of_type,
                    stringResource(uiState.eventType.stringResourceId)
                ),
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

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .weight(0.8f)

        ) {
            items(uiState.gradesByTypeListItems, key = { it.eventSummary }) {
                GradeRow(
                    eventType = uiState.eventType,
                    gradesByTypeListItem = it,
                    onSummaryAndTypeClick = onSummaryAndTypeClick
                )
            }
        }
    }
}


@Composable
fun GradeRow(
    eventType: Event.Type,
    gradesByTypeListItem: GradesByTypeListItem,
    onSummaryAndTypeClick: (summary: String, type: Event.Type) -> Unit
) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .clip(shape = RoundedCornerShape(4.dp))
            .padding(6.dp),
        border = androidx.compose.foundation.BorderStroke(
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
                .clickable {
                    onSummaryAndTypeClick(gradesByTypeListItem.eventSummary, eventType)
                },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${stringResource(eventType.stringResourceId)}\n${gradesByTypeListItem.eventSummary}",
                modifier = Modifier
                    .padding(4.dp)
                    .weight(1.5f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = stringResource(
                    R.string.graded,
                    gradesByTypeListItem.gradedCount,
                    gradesByTypeListItem.totalCount
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(4.dp)
                    .weight(0.9f)
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_right),
                contentDescription = stringResource(
                    R.string.access_type_grades,
                    eventType
                ),
                modifier = Modifier
                    .size(48.dp)
                    .padding(8.dp)
            )
        }
    }
}

@Preview
@Composable
fun GradesPreview() {
    PrepPalTheme {
        GradesByTypeScreen(
            uiState = GradesByTypeScreenUiState(
                Event.Type.Lecture
            )
        )
    }
}