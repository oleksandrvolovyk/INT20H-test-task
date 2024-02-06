package the_null_pointer.preppal.ui

import the_null_pointer.preppal.R

sealed class NavItem(var screenRoute: String) {
    object NewEvent : NavItem("new-event")
}

sealed class BottomNavItem(var title: String, var icon: Int, var screenRoute: String) {
    object Calendar : BottomNavItem("Calendar", R.drawable.ic_calendar, "calendar")
    object Grades : BottomNavItem("Grades", R.drawable.ic_grade, "grades")
}