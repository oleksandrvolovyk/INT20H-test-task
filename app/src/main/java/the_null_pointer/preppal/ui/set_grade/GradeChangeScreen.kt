package the_null_pointer.preppal.ui.set_grade

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import the_null_pointer.preppal.R

@Composable
fun GradeChangeScreen(onBackClicked: () -> Unit = {}) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                state = scrollState,
                enabled = true
            )
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ){
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ){
                Button(modifier = Modifier
                    .width(65.dp)
                    .height(35.dp)
                    .align(Alignment.TopStart),
                    onClick =  onBackClicked,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    )) {
                    Icon(painter = painterResource(id = R.drawable.ic_arrow_left),
                        contentDescription = "Arrow icon")
                }
                Text(
                    text = "Оцінка - ВСТАВИТИ З БД"
                )
            }
        }

        SimpleText(text = "Вставити ЗАВДАННЯ з Бази даних")
        SimpleText(text = "ДАТА")
        SimpleText(text = "Введіть ващу оцінку")

        // Move to ViewModel !!

        val getGradeState = remember { mutableStateOf("") }
        val maxGradeState = remember { mutableStateOf("") }

        Row {
            TextField(
                value = getGradeState.value,
                onValueChange = { newText -> getGradeState.value = newText },
                modifier = Modifier.weight(0.5f),
                label = { Text("Отримана оцінка") } // Optional: Add a label for the text field
            )

            Spacer(modifier = Modifier.width(5.dp))

            TextField(
                value = maxGradeState.value,
                onValueChange = { newText -> maxGradeState.value = newText },
                modifier = Modifier.weight(0.5f),
                label = { Text("Максимальна оцінка") } // Optional: Add a label for the text field
            )
        }



    }
}

@Composable
fun SimpleText(
    text: String
){
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
//            .clip(shape = CutCornerShape(1.dp))
            .padding(6.dp),
        border =  BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colors.onPrimary
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp)

    ){
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
    GradeChangeScreen()
}
