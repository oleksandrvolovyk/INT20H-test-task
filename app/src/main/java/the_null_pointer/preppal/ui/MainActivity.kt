package the_null_pointer.preppal.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import the_null_pointer.preppal.R
import the_null_pointer.preppal.ui.calendar.Calendar
import the_null_pointer.preppal.ui.grades.Grades
import the_null_pointer.preppal.ui.new_event.NewEvent
import the_null_pointer.preppal.ui.theme.PrepPalTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PrepPalTheme {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = { MyBottomNavigation(navController = navController) }
                ) { contentPadding ->
                    NavigationGraph(navController = navController, contentPadding = contentPadding)
                }
            }
        }
    }
}

@Composable
fun NavigationGraph(navController: NavHostController, contentPadding: PaddingValues) {
    NavHost(
        modifier = Modifier.padding(contentPadding),
        navController = navController,
        startDestination = BottomNavItem.Calendar.screenRoute,
    ) {
        composable(BottomNavItem.Calendar.screenRoute) {
            Calendar(onNewEventButtonClick = { navController.navigate(NavItem.NewEvent.screenRoute) })
        }
        composable(BottomNavItem.Grades.screenRoute) {
            Grades()
        }
        composable(NavItem.NewEvent.screenRoute) {
            NewEvent()
        }
    }
}

@Composable
fun MyBottomNavigation(navController: NavController) {
    val items = listOf(
        BottomNavItem.Calendar,
        BottomNavItem.Grades
    )
    BottomNavigation(
        backgroundColor = colorResource(id = R.color.teal_200),
        contentColor = Color.Black
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(painterResource(id = item.icon), contentDescription = item.title) },
                selectedContentColor = Color.Black,
                unselectedContentColor = Color.Black.copy(0.4f),
                selected = currentRoute == item.screenRoute,
                onClick = {
                    navController.navigate(item.screenRoute) {

                        navController.graph.startDestinationRoute?.let { screen_route ->
                            popUpTo(screen_route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}