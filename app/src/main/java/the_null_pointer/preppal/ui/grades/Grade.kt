package the_null_pointer.preppal.ui.grades

data class Grade (
    val title: String,
    val date: String,
    var grade: String = "-",
    var maxGrade: String = "-",
    var visited: String = "-",
    var maxVisits: String = "-"
)