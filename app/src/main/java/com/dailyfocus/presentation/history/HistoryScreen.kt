package com.dailyfocus.presentation.history

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dailyfocus.core.ui.components.DailyFocusScaffold
import com.dailyfocus.core.ui.components.EmptyState
import com.dailyfocus.core.ui.theme.Elevation
import com.dailyfocus.core.ui.theme.Spacing
import com.dailyfocus.domain.model.HistoryItem
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    onNavigateBack: () -> Unit,
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    DailyFocusScaffold(
        topBar = {
            TopAppBar(
                title = { Text("Activity History") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (state.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (state.historyItems.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                EmptyState(
                    message = "No history yet",
                    subMessage = "Complete tasks or log focus sessions to see them here."
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(bottom = Spacing.maximize)
            ) {
                state.historyItems.forEach { (date, items) ->
                    stickyHeader {
                        DateHeader(date, items.size)
                    }
                    items(items) { item ->
                        HistoryItemRow(item)
                    }
                }
            }
        }
    }
}

@Composable
private fun DateHeader(date: LocalDate, count: Int) {
    Surface(
        color = MaterialTheme.colorScheme.surface, // Background for sticky header
        contentColor = MaterialTheme.colorScheme.onSurface,
        tonalElevation = Elevation.Level2,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.m, vertical = Spacing.s),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)),
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )
            
            Text(
                text = "$count entries",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    }
}

@Composable
private fun HistoryItemRow(item: HistoryItem) {
    ListItem(
        headlineContent = {
            when (item) {
                is HistoryItem.ProcessedTask -> {
                    Text(
                        text = item.task.title,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                is HistoryItem.ProcessedHabit -> {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                is HistoryItem.ProcessedFocusSession -> {
                    Text(
                        text = item.entry.activityName,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        },
        supportingContent = {
             when (item) {
                is HistoryItem.ProcessedTask -> {
                     Text("Task • ${item.task.category.name.lowercase().capitalize()}")
                }
                is HistoryItem.ProcessedHabit -> {
                    Text("Habit Completed")
                }
                is HistoryItem.ProcessedFocusSession -> {
                    val m = item.entry.durationMinutes
                    val duration = if (m >= 60) "${m/60}h ${m%60}m" else "${m}m"
                    Text("Focus Session • $duration")
                }
            }
        },
        leadingContent = {
            val icon = when (item) {
                is HistoryItem.ProcessedTask -> androidx.compose.material.icons.Icons.Default.Check
                is HistoryItem.ProcessedHabit -> androidx.compose.material.icons.Icons.Default.Refresh
                is HistoryItem.ProcessedFocusSession -> androidx.compose.material.icons.Icons.Default.Timer
            }
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    )
    Divider(
        thickness = 0.5.dp, 
        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
        modifier = Modifier.padding(horizontal = Spacing.m)
    )
}

// Helper to capitalize category names
private fun String.capitalize() = replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
