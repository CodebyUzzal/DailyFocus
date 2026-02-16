package com.dailyfocus.presentation.habits

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dailyfocus.core.ui.theme.Spacing
import com.dailyfocus.presentation.habits.components.CalendarHeatmap
import com.dailyfocus.presentation.habits.components.MomentumCard
import com.dailyfocus.presentation.habits.components.StatsRow
import com.dailyfocus.presentation.habits.components.HabitEmptyState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitDetailScreen(
    habitId: Long,
    onNavigateBack: () -> Unit,
    viewModel: HabitDetailViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.habit?.habit?.title ?: "Habit Detail") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (state.totalLogs == 0) {
            HabitEmptyState(
                onMarkToday = { viewModel.toggleHabitLog(java.time.LocalDate.now()) },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(top = 16.dp, start = 0.dp, end = 0.dp, bottom = 96.dp), // Zero Hz padding for full width, handled inside items
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item {
                    state.habit?.let { habitWithStreak ->
                        // 1. Hero Streak (Momentum)
                        MomentumCard(
                            currentStreak = habitWithStreak.currentStreak,
                            longestStreak = habitWithStreak.longestStreak,
                            modifier = Modifier.padding(horizontal = Spacing.m)
                        )
                    }
                }

                item {
                    state.habit?.let {
                        // 2. Stats Row
                        StatsRow(
                            completionRate = state.completionRate,
                            currentWeekCount = state.currentWeekCount,
                            totalLogs = state.totalLogs,
                            modifier = Modifier.padding(horizontal = Spacing.m)
                        )
                    }
                }

                item {
                    // 3. Section Header
                     Text(
                        text = "Consistency",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(horizontal = Spacing.m)
                    )
                }

                item {
                    state.habit?.let {
                        // 4. Calendar heatmap
                        CalendarHeatmap(
                            completedDates = state.completedDates,
                            onDayClick = { date -> viewModel.toggleHabitLog(date) },
                            modifier = Modifier.padding(horizontal = Spacing.m),
                            currentMonth = state.selectedMonth,
                            onMonthChanged = viewModel::onMonthChanged,
                            startDate = state.habit?.habit?.createdAt?.toLocalDate()
                        )
                    }
                }
            }
        }
    }
}
