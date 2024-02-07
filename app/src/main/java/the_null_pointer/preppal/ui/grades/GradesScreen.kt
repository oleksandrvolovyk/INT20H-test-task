package the_null_pointer.preppal.ui.grades

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material3.Button

import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import the_null_pointer.preppal.R
import java.util.Random
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import the_null_pointer.preppal.data.Event
import the_null_pointer.preppal.data.Event.Type.Companion.stringResourceId
import the_null_pointer.preppal.ui.calendar.CalendarScreen
import the_null_pointer.preppal.ui.calendar.CalendarScreenUiState
import the_null_pointer.preppal.ui.theme.PrepPalTheme

@Composable
fun GradesScreen(uiState: GradesScreenUiState, onTypeClick: (String) -> Unit = {}) {

    val scrollState = rememberScrollState()
    var expanded by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                state = scrollState,
                enabled = true
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.scoreBoard),
                    fontSize = 20.sp,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(6.dp)
                )

                Button(
                    modifier = Modifier
                        .width(65.dp)
                        .height(40.dp)
                        .align(Alignment.TopStart),
                    onClick = { expanded = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    )
                ) {
                    Text(
                        "...",
                        fontSize = 15.sp
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {

                    // Add Sorting like show ONLY ENGLISH!!

                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.sorting)) },
                        onClick = { Log.d("GradesScreen", "${uiState.events}") }
                    )
                    DropdownMenuItem(
                        text = { Text("Settings") },
                        onClick = { /* Handle settings! */ }
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            items(uiState.events, key = { grade -> grade.id })
            { grade ->

                // Змінити умову на перевірку саме чи оцінюване завдання

                if (!grade.graded) {
                    GradeRow(grade, onTypeClick)
                }

            }
        }

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                stringResource(R.string.progress),
                fontSize = 20.sp,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(6.dp)
            )
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            items(uiState.events, key = { grade -> grade.type })
            { grade ->
                ProgressRow(grade)
            }
        }

    }

}


@Composable
fun GradeRow(grade: Event, onTypeClick: (String) -> Unit = {}) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .clip(shape = RoundedCornerShape(4.dp))
            .padding(6.dp),
        border = androidx.compose.foundation.BorderStroke(
            width = 3.dp,
            color = colors.secondary
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)

                //Add navigation in clickable !->

                .clickable(onClick = { onTypeClick("${grade.type}") }),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,

            ) {

            Text(
                text = stringResource(id = grade.type.stringResourceId),
                modifier = Modifier
                    .padding(4.dp)
                    .weight(1.8f)
            )
            Text(
                text = "",
                modifier = Modifier
                    .padding(4.dp)
                    .weight(0.9f)
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_right),
                contentDescription = "Access Time",
                tint = Color.Black,
                modifier = Modifier
                    .size(48.dp)
                    .padding(8.dp)
            )
        }
    }
}

@Composable
fun ProgressRow(grade: Event) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .clip(shape = RoundedCornerShape(4.dp))
            .padding(6.dp),
        border = androidx.compose.foundation.BorderStroke(
            width = 3.dp,
            color = colors.secondary
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {


            Text(
                text = stringResource(id = grade.type.stringResourceId),
                modifier = Modifier
                    .padding(4.dp)
                    .weight(0.8f)
            )
            Text(
                text = "Відвідано\n${grade.grade ?: "-"} / ${grade.maxGrade ?: "-"}",
                modifier = Modifier
                    .padding(4.dp)
            )
        }
    }
}


@Preview
@Composable
fun GradesPreview() {
    PrepPalTheme {
        GradesScreen(
            uiState = GradesScreenUiState(

            )
        )
    }
}
