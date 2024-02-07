package the_null_pointer.preppal.ui.grades_by_type

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
import the_null_pointer.preppal.data.Event
import the_null_pointer.preppal.ui.calendar.CalendarScreen
import the_null_pointer.preppal.ui.grades.GradesScreenUiState
import the_null_pointer.preppal.ui.theme.PrepPalTheme

@Composable
fun GradesByTypeScreen(uiState: GradesByTypeScreenUiState,
                       onGradeClick: () -> Unit = {},
                       onBackClick: () -> Unit = {}) {

    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                state = scrollState,
                enabled = true
            )
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()) {
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

                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_left),
                        contentDescription = "Access Time",
                        tint = Color.Black,
                        modifier = Modifier.width(50.dp)
                            .align(Alignment.CenterStart)
                            .clickable {  onBackClick() }// Fill the available space
                    )

            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .weight(0.8f)

        ) {
            items(uiState.events, key = { grade -> grade.id })
            { grade ->

                // Змінити умову на перевірку саме чи оцінюване завдання

                if(!grade.graded){
                    GradeRow(grade, onGradeClick)
                }
            }
        }
    }
}


@Composable
fun GradeRow(grade: Event, onGradeClick: () -> Unit = {} ){
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .clip(shape = RoundedCornerShape(4.dp))
            .padding(6.dp),
        border =  androidx.compose.foundation.BorderStroke(
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

                .clickable (onClick = onGradeClick)
            ,
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,

            ) {

            Text(text = "${grade.type}\n${grade.summary}",
                modifier = Modifier
                    .padding(4.dp)
                    .weight(0.8f))
            Text(text = grade.end.toString(),
                modifier = Modifier
                    .padding(4.dp)
                    .weight(0.9f))
            Text(text = "Оцінка\n${grade.grade}/${grade.maxGrade}",
                modifier = Modifier
                    .padding(4.dp))
        }
    }
}

@Preview
@Composable
fun GradesPreview() {
    PrepPalTheme {
        GradesByTypeScreen(
            uiState = GradesByTypeScreenUiState(

            )
        )
    }
}