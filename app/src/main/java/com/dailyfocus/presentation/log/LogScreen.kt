package com.dailyfocus.presentation.log

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dailyfocus.core.ui.components.DailyFocusCard
import com.dailyfocus.core.ui.components.DailyFocusScaffold
import com.dailyfocus.core.ui.components.EmptyState
import com.dailyfocus.core.ui.components.PremiumExtendedFAB
import com.dailyfocus.core.ui.theme.AppShapes
import com.dailyfocus.core.ui.theme.Elevation
import com.dailyfocus.core.ui.theme.Spacing
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dailyfocus.domain.model.DailyLogEntry
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import com.dailyfocus.presentation.navigation.Destinations
import androidx.compose.material.icons.filled.List

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun LogScreen(
    onNavigateToHistory: () -> Unit,
    viewModel: LogViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showAddDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.userMessage) {
        state.userMessage?.let {
            snackbarHostState.showSnackbar(it.text)
            viewModel.onMessageDismissed()
        }
    }

    // Group entries by date
    val groupedEntries = remember(state.entries) {
        state.entries.groupBy { it.date }
    }

    DailyFocusScaffold(
        topBar = {
             Column(modifier = Modifier.padding(top = Spacing.l, bottom = Spacing.s)) {
                 Row(
                     modifier = Modifier
                         .fillMaxWidth()
                         .padding(horizontal = Spacing.m),
                     horizontalArrangement = Arrangement.SpaceBetween,
                     verticalAlignment = Alignment.CenterVertically
                 ) {
                     Text(
                         text = "Journal", 
                         style = MaterialTheme.typography.headlineLarge,
                         color = MaterialTheme.colorScheme.primary
                     )
                     // Stats Summary Mini (Total Hours)
                     Surface(
                         shape = RoundedCornerShape(16.dp),
                         color = MaterialTheme.colorScheme.surfaceVariant
                     ) {
                         Row(
                             modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                             verticalAlignment = Alignment.CenterVertically
                         ) {
                             Icon(Icons.Default.Schedule, null, modifier = Modifier.size(16.dp))
                             Spacer(modifier = Modifier.width(6.dp))
                             Text(
                                 text = formatDuration(state.entries.sumOf { it.durationMinutes }),
                                 style = MaterialTheme.typography.labelLarge
                             )
                         }
                     }
                 }
                 
                 // Filter Chips
                 Row(
                     modifier = Modifier
                         .fillMaxWidth()
                         .horizontalScroll(rememberScrollState())
                         .padding(horizontal = Spacing.m, vertical = Spacing.s),
                     horizontalArrangement = Arrangement.spacedBy(8.dp)
                 ) {
                     FilterChip(
                         selected = state.filter == null,
                         onClick = { viewModel.onFilterChanged(null) },
                         label = { Text("All") }
                     )
                     com.dailyfocus.domain.model.LogType.entries.forEach { type ->
                        FilterChip(
                            selected = state.filter == type,
                            onClick = { viewModel.onFilterChanged(type) },
                            label = { Text(type.name.lowercase().replaceFirstChar { it.uppercase() }) }
                        )
                     }
                 }
             }
        },
        floatingActionButton = {
            PremiumExtendedFAB(
                onClick = { showAddDialog = true },
                text = { Text("Log Entry") },
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        if (state.entries.isEmpty() && !state.isLoading) {
             Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                EmptyState(
                    message = "No journal entries.",
                    subMessage = "Log your focus sessions or habits.",
                    icon = Icons.Default.Schedule
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(bottom = Spacing.maximize),
                verticalArrangement = Arrangement.spacedBy(Spacing.m)
            ) {
                groupedEntries.forEach { (date, entries) ->
                    stickyHeader {
                        Surface(
                            color = MaterialTheme.colorScheme.background,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                             Column(modifier = Modifier.padding(horizontal = Spacing.m, vertical = Spacing.s)) {
                                 Row(verticalAlignment = Alignment.CenterVertically) {
                                     Text(
                                        text = if (date == java.time.LocalDate.now()) "Today" else date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)),
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.primary
                                     )
                                     Spacer(modifier = Modifier.weight(1f))
                                     Text(
                                         text = formatDuration(entries.sumOf { it.durationMinutes }),
                                         style = MaterialTheme.typography.labelMedium,
                                         color = MaterialTheme.colorScheme.onSurfaceVariant
                                     )
                                 }
                                 Divider(modifier = Modifier.padding(top = Spacing.xs))
                             }
                        }
                    }

                    items(entries, key = { it.id }) { entry ->
                        LogEntryCard(entry = entry, onDelete = { viewModel.onDeleteEntry(entry.id) })
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddLogDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { activity, duration, type, note ->
                viewModel.onAddEntry(activity, duration, type, note)
                showAddDialog = false
            }
        )
    }
}

@Composable
private fun LogEntryCard(entry: DailyLogEntry, onDelete: () -> Unit) {
    DailyFocusCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.m),
        elevation = Elevation.Level1,
        shape = AppShapes.Medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.m),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Type Icon
            Surface(
                shape = androidx.compose.foundation.shape.CircleShape,
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = when(entry.type) {
                            com.dailyfocus.domain.model.LogType.FOCUS -> Icons.Default.Schedule
                            com.dailyfocus.domain.model.LogType.HABIT -> Icons.Default.LocalFireDepartment /* Or similar */
                            com.dailyfocus.domain.model.LogType.TASK -> Icons.Default.Check
                        },
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(Spacing.m))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = entry.activityName,
                    style = MaterialTheme.typography.titleMedium
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
                    modifier = Modifier.padding(top = Spacing.xxs),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = formatDuration(entry.durationMinutes),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        text = "•",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                     Text(
                        text = entry.type.name.lowercase().replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                entry.note?.let { note ->
                    if (note.isNotBlank()) {
                         Text(
                            text = note,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = Spacing.xs)
                        )
                    }
                }
            }
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

private fun formatDuration(minutes: Int): String {
    val h = minutes / 60
    val m = minutes % 60
    return if (h > 0) "${h}h ${m}m" else "${m}m"
}

@Composable
private fun AddLogDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, Int, com.dailyfocus.domain.model.LogType, String?) -> Unit
) {
    var activity by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(com.dailyfocus.domain.model.LogType.FOCUS) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Log Activity") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = activity,
                    onValueChange = { activity = it },
                    label = { Text("Activity") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                     com.dailyfocus.domain.model.LogType.entries.forEach { type ->
                        FilterChip(
                            selected = selectedType == type,
                            onClick = { selectedType = type },
                            label = { Text(type.name.lowercase().replaceFirstChar { it.uppercase() }) }
                        )
                     }
                }

                OutlinedTextField(
                    value = duration,
                    onValueChange = { duration = it.filter { c -> c.isDigit() } },
                    label = { Text("Duration (minutes)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Note (optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val dur = duration.toIntOrNull() ?: 0
                    if (activity.isNotBlank() && dur > 0) {
                        onConfirm(activity, dur, selectedType, note.ifBlank { null })
                    }
                },
                enabled = activity.isNotBlank() && (duration.toIntOrNull() ?: 0) > 0
            ) { Text("Log") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}
