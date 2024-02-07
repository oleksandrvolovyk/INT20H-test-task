package the_null_pointer.preppal.ui.set_grade

import androidx.compose.runtime.Composable
import the_null_pointer.preppal.ui.grades.GradesScreen

@Composable
fun GradeChange(onBackClicked: () -> Unit) {
    GradeChangeScreen( onBackClicked = onBackClicked)
}