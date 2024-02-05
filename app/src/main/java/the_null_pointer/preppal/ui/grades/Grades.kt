package the_null_pointer.preppal.ui.grades

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Random

@Composable
fun Grades() {

    val grades = List(6) {
        Grade(
            title = "Комп'ютерні системи ${it + 1}",
            date = "2024-жовтня-${20 + it}",     // Generating dates in February 2024
            grade = "${(60 + Random().nextInt(40))}%", // Random grades between 60% and 100%
            maxGrade = "100%"
        )
    }
    Column {
        Box (
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ){
            Text("Журнал оцінок",
                fontSize = 20.sp,
                modifier= Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .padding(6.dp))
        }


        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) { items( grades,  key = {  grade -> grade.date })
        { grade ->
            GradeRow(grade)
        }
        }

        Box (
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ){
            Text("Прогрес",
                fontSize = 20.sp,
                modifier= Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .padding(6.dp))
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) { items( grades,  key = {  grade -> grade.title })
        { grade ->
            ProgressRow(grade)
        }
        }
    }

}

@Composable
fun GradeRow(grade: Grade){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp) // Adjust height as needed
            .clip(shape = RoundedCornerShape(4.dp)) // Thinner corners (4.dp instead of 6.dp)
            .background(Color.White) // Fill with white color
            .padding(6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {


            Text(text = grade.title,
                modifier = Modifier
                    .padding(4.dp)
                    .weight(0.8f))
            Text(text = grade.date,
                modifier = Modifier
                    .padding(4.dp)
                    .weight(0.9f))
            Text(text = "Оцінка\n${grade.grade}/${grade.maxGrade}",
                modifier = Modifier
                    .padding(4.dp))
        }
    }
}

@Composable
fun ProgressRow(grade: Grade){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp) // Adjust height as needed
            .clip(shape = RoundedCornerShape(4.dp)) // Thinner corners (4.dp instead of 6.dp)
            .background(Color.White) // Fill with white color
            .padding(6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {


            Text(text = grade.title,
                modifier = Modifier
                    .padding(4.dp)
                    .weight(0.8f))
            Text(text = "Відвідано\n${grade.grade}/${grade.maxGrade}",
                modifier = Modifier
                    .padding(4.dp))
        }
    }
}


@Preview
@Composable
fun GradesPreview() {
    Grades()
}
