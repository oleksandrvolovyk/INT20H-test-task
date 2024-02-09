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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import the_null_pointer.preppal.ui.calendar.Calendar
import the_null_pointer.preppal.ui.event.add_new.NewEvent
import the_null_pointer.preppal.ui.event.edit.EditEvent
import the_null_pointer.preppal.ui.grades.Grades
import the_null_pointer.preppal.ui.grades_by_type.GradesByType
import the_null_pointer.preppal.ui.progress.by_summary_and_type.ProgressBySummaryAndType
import the_null_pointer.preppal.ui.progress.by_type.ProgressByType
import the_null_pointer.preppal.ui.set_grade.GradeChange
import the_null_pointer.preppal.ui.theme.PrepPalTheme
import the_null_pointer.preppal.util.TimeUtil.MILLISECONDS_IN_DAY
import the_null_pointer.preppal.util.UiUtil.defaultEnterTransition
import the_null_pointer.preppal.util.UiUtil.defaultExitTransition
import the_null_pointer.preppal.util.UiUtil.defaultPopEnterTransition
import the_null_pointer.preppal.util.UiUtil.defaultPopExitTransition
import the_null_pointer.preppal.util.UiUtil.defaultVerticalEnterTransition
import the_null_pointer.preppal.util.UiUtil.defaultVerticalExitTransition
import the_null_pointer.preppal.util.UiUtil.defaultVerticalPopEnterTransition
import the_null_pointer.preppal.util.UiUtil.defaultVerticalPopExitTransition

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
        composable(
            route = BottomNavItem.Calendar.screenRoute,
            enterTransition = { defaultEnterTransition },
            exitTransition = {
                if (targetState.destination.route in listOf(
                        NavItem.EditEvent.screenRoute + "/{eventId}",
                        NavItem.NewEvent.screenRoute + "?startingEpochDay={startingEpochDay}"
                    )
                )
                    defaultVerticalExitTransition
                else
                    defaultExitTransition
            },
            popEnterTransition = {
                if (initialState.destination.route in listOf(
                        NavItem.EditEvent.screenRoute + "/{eventId}",
                        NavItem.NewEvent.screenRoute + "?startingEpochDay={startingEpochDay}"
                    )
                )
                    defaultVerticalPopEnterTransition
                else
                    defaultPopEnterTransition
            },
            popExitTransition = { defaultPopExitTransition }
        ) {
            Calendar(
                onNewEventButtonClick = {
                    navController.navigate(NavItem.NewEvent.screenRoute + "?startingEpochDay=$it")
                },
                onEventClick = {
                    navController.navigate(NavItem.EditEvent.screenRoute + "/$it")
                },
                onEventGradeClick = {
                    navController.navigate(NavItem.GradeChange.screenRoute + "/$it")
                }
            )
        }
        composable(
            route = BottomNavItem.Grades.screenRoute,
            enterTransition = { defaultEnterTransition },
            exitTransition = { defaultExitTransition },
            popEnterTransition = { defaultPopEnterTransition },
            popExitTransition = { defaultPopExitTransition }
        ) {
            Grades(
                onGradesTypeClick = { type ->
                    navController.navigate(NavItem.GradesByType.screenRoute + "/" + type)
                },
                onProgressTypeClick = { type ->
                    navController.navigate(NavItem.ProgressByType.screenRoute + "/" + type)
                }
            )
        }
        composable(
            route = NavItem.GradesByType.screenRoute + "/{type}",
            arguments = listOf(navArgument("type") { type = NavType.StringType }),
            enterTransition = { defaultEnterTransition },
            exitTransition = { defaultExitTransition },
            popEnterTransition = { defaultPopEnterTransition },
            popExitTransition = { defaultPopExitTransition }
        ) { backStackEntry ->
            backStackEntry?.arguments?.getString("type")?.let { type ->

                GradesByType(
                    onGradeClick = { id -> navController.navigate(NavItem.GradeChange.screenRoute + "/" + id) },
                    onBackClick = { navController.popBackStack() },
                    type = type
                )
            }
        }
        composable(
            route = NavItem.ProgressByType.screenRoute + "/{type}",
            arguments = listOf(navArgument("type") { type = NavType.StringType }),
            enterTransition = { defaultEnterTransition },
            exitTransition = { defaultExitTransition },
            popEnterTransition = { defaultPopEnterTransition },
            popExitTransition = { defaultPopExitTransition }
        ) {
            ProgressByType(
                onSummaryAndTypeClick = { summary, type ->
                    navController.navigate(NavItem.ProgressBySummaryAndType.screenRoute + "/$type/$summary")
                },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(
            route = NavItem.ProgressBySummaryAndType.screenRoute + "/{type}/{summary}",
            arguments = listOf(
                navArgument("type") { type = NavType.StringType },
                navArgument("summary") { type = NavType.StringType }
            ),
            enterTransition = { defaultEnterTransition },
            exitTransition = { defaultExitTransition },
            popEnterTransition = { defaultPopEnterTransition },
            popExitTransition = { defaultPopExitTransition }
        ) {
            ProgressBySummaryAndType(
                onEventClick = { navController.navigate(NavItem.GradeChange.screenRoute + "/$it") },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(
            NavItem.GradeChange.screenRoute + "/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType }),
            enterTransition = { defaultEnterTransition },
            exitTransition = { defaultExitTransition },
            popEnterTransition = { defaultPopEnterTransition },
            popExitTransition = { defaultPopExitTransition }
        ) {
            GradeChange(onBackClicked = { navController.popBackStack() })
        }
        composable(
            NavItem.NewEvent.screenRoute + "?startingEpochDay={startingEpochDay}",
            arguments = listOf(navArgument("startingEpochDay") {
                type = NavType.LongType
                defaultValue = System.currentTimeMillis() / MILLISECONDS_IN_DAY
            }),
            enterTransition = { defaultVerticalEnterTransition },
            exitTransition = { defaultVerticalExitTransition },
            popEnterTransition = { defaultVerticalPopEnterTransition },
            popExitTransition = { defaultVerticalPopExitTransition }
        ) {
            NewEvent(onNavigateBack = { navController.popBackStack() })
        }
        composable(
            NavItem.EditEvent.screenRoute + "/{eventId}",
            arguments = listOf(navArgument("eventId") { type = NavType.LongType }),
            enterTransition = { defaultVerticalEnterTransition },
            exitTransition = { defaultVerticalExitTransition },
            popEnterTransition = { defaultVerticalPopEnterTransition },
            popExitTransition = { defaultVerticalPopExitTransition }
        ) {
            EditEvent(onNavigateBack = { navController.popBackStack() })
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
        backgroundColor = MaterialTheme.colorScheme.surface,
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