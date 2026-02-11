package com.dailyfocus.presentation.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.dailyfocus.presentation.goals.GoalsScreen
import com.dailyfocus.presentation.habits.HabitDetailScreen
import com.dailyfocus.presentation.habits.HabitsScreen
import com.dailyfocus.presentation.log.LogScreen
import com.dailyfocus.presentation.today.TodayScreen
import com.dailyfocus.presentation.today.routines.RoutineListScreen

/**
 * Root composable providing bottom navigation and NavHost.
 */
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStack?.destination

    // Only show bottom bar on main tabs
    val showBottomBar = Screen.bottomNavItems.any { screen ->
        currentDestination?.hierarchy?.any { it.route == screen.route } == true
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    Screen.bottomNavItems.forEach { screen ->
                        val isSelected = currentDestination?.hierarchy?.any {
                            it.route == screen.route
                        } == true

                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = screen.icon,
                                    contentDescription = screen.label
                                )
                            },
                            label = { Text(screen.label) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Today.route,
            modifier = Modifier.padding(innerPadding),
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            // ── Today ───────────────────────────────────────────────
            composable(Screen.Today.route) {
                TodayScreen(
                    onNavigateToRoutines = {
                        navController.navigate(Destinations.ROUTINES)
                    }
                )
            }

            composable(Destinations.ROUTINES) {
                RoutineListScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // ── Habits ──────────────────────────────────────────────
            composable(Screen.Habits.route) {
                HabitsScreen(
                    onNavigateToDetail = { habitId ->
                        navController.navigate(Destinations.habitDetail(habitId))
                    }
                )
            }

            composable(
                route = Destinations.HABIT_DETAIL,
                arguments = listOf(navArgument("habitId") { type = NavType.LongType })
            ) { backStackEntry ->
                val habitId = backStackEntry.arguments?.getLong("habitId") ?: return@composable
                HabitDetailScreen(
                    habitId = habitId,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // ── Goals ───────────────────────────────────────────────
            composable(Screen.Goals.route) {
                GoalsScreen()
            }

            // ── Log ─────────────────────────────────────────────────
            composable(Screen.Log.route) {
                LogScreen()
            }
        }
    }
}
