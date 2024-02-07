package the_null_pointer.preppal.ui.set_grade

import androidx.compose.runtime.Composable
import the_null_pointer.preppal.data.Event

@Composable
fun GradeChange(event:Event, onBackClicked: () -> Unit) {
    GradeChangeScreen(event = event,  onBackClicked = onBackClicked)
}