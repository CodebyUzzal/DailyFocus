package com.dailyfocus.presentation.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.dailyfocus.core.preferences.AppPreferences
import com.dailyfocus.core.ui.theme.AnimationConstants
import com.dailyfocus.presentation.goals.GoalsScreen
import com.dailyfocus.presentation.habits.HabitDetailScreen
import com.dailyfocus.presentation.habits.HabitsScreen
import com.dailyfocus.presentation.log.LogScreen
import com.dailyfocus.presentation.onboarding.OnboardingScreen
import com.dailyfocus.presentation.today.TodayScreen
import com.dailyfocus.presentation.today.routines.RoutineListScreen
import javax.inject.Inject

/**
 * Root composable providing bottom navigation and NavHost.
 * Checks onboarding state to determine start destination.
 */
@Composable
fun MainScreen(appPreferences: AppPreferences) {
    val isOnboardingComplete by appPreferences.isOnboardingComplete
        .collectAsState(initial = null) // null = still loading

    // Show nothing while loading preference
    if (isOnboardingComplete == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val startDestination = if (isOnboardingComplete == true) {
        Screen.Today.route
    } else {
        Destinations.ONBOARDING
    }

    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStack?.destination

    // Only show bottom bar on main tabs (not onboarding or detail screens)
    val showBottomBar = Screen.bottomNavItems.any { screen ->
        currentDestination?.hierarchy?.any { it.route == screen.route } == true
    }

    var showSettingsDialog by remember { mutableStateOf(false) }

    if (showSettingsDialog) {
        com.dailyfocus.core.ui.components.SettingsDialog(
            onDismiss = { showSettingsDialog = false },
            appPreferences = appPreferences
        )
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
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding),
            enterTransition = { AnimationConstants.screenEnter },
            exitTransition = { AnimationConstants.screenExit },
            popEnterTransition = { AnimationConstants.screenPopEnter },
            popExitTransition = { AnimationConstants.screenPopExit }
        ) {
            // ── Onboarding ──────────────────────────────────────────
            composable(Destinations.ONBOARDING) {
                OnboardingScreen(
                    onComplete = {
                        navController.navigate(Screen.Today.route) {
                            popUpTo(Destinations.ONBOARDING) { inclusive = true }
                        }
                    }
                )
            }

            // ── Today ───────────────────────────────────────────────
            composable(Screen.Today.route) {
                TodayScreen(
                    onNavigateToRoutines = {
                        navController.navigate(Destinations.ROUTINES)
                    },
                    onOpenSettings = {
                        showSettingsDialog = true
                    },
                    onNavigateToAbout = {
                        navController.navigate(Destinations.ABOUT)
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
                LogScreen(
                    onNavigateToHistory = {
                        navController.navigate(Destinations.HISTORY)
                    }
                )
            }

            composable(Destinations.HISTORY) {
                com.dailyfocus.presentation.history.HistoryScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(Destinations.ABOUT) {
                com.dailyfocus.presentation.about.AboutScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}
