package com.dailyfocus.presentation.habits

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dailyfocus.presentation.habits.components.CalendarHeatmap

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
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                state.habit?.let { habitWithStreak ->
                    // Streak cards
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StreakCard(
                            label = "Current Streak",
                            value = habitWithStreak.currentStreak,
                            modifier = Modifier.weight(1f)
                        )
                        StreakCard(
                            label = "Longest Streak",
                            value = habitWithStreak.longestStreak,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Calendar heatmap
                    CalendarHeatmap(completedDates = state.completedDates)
                }
            }
        }
    }
}

@Composable
private fun StreakCard(
    label: String,
    value: Int,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.LocalFireDepartment,
                contentDescription = null,
                tint = if (value > 0)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "$value",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
