package com.dailyfocus.presentation.log

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogScreen(
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

    // Calculate insights from entries (UI logic only)
    val totalMinutes = remember(state.entries) { state.entries.sumOf { it.durationMinutes } }
    val entryCount = state.entries.size

    DailyFocusScaffold(
        topBar = {
             Box(modifier = Modifier.padding(top = Spacing.l, start = Spacing.m, bottom = Spacing.m)) {
                 Text(
                     text = "History", 
                     style = MaterialTheme.typography.headlineLarge,
                     color = MaterialTheme.colorScheme.primary
                 )
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
                    message = "No time logged yet.",
                    subMessage = "Track your focus sessions.",
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
                // Dashboard Summary
                item {
                    DailyFocusCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Spacing.m),
                        elevation = Elevation.Level0,
                        shape = AppShapes.Large,
                        onClick = {} // Non-clickable
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(Spacing.l),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = formatDuration(totalMinutes),
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = "Total Focus",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "$entryCount",
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                                Text(
                                    text = "Sessions",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                item {
                    Text(
                        text = "Recent Entries",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = Spacing.m, vertical = Spacing.xs),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                items(state.entries, key = { it.id }) { entry ->
                    LogEntryCard(entry = entry, onDelete = { viewModel.onDeleteEntry(entry.id) })
                }
            }
        }
    }

    if (showAddDialog) {
        AddLogDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { activity, duration, note ->
                viewModel.onAddEntry(activity, duration, note)
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
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = entry.activityName,
                    style = MaterialTheme.typography.titleMedium
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
                    modifier = Modifier.padding(top = Spacing.xxs)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Schedule,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(Spacing.xxs))
                        Text(
                            text = formatDuration(entry.durationMinutes),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(
                        text = "•",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = entry.date.format(
                            DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
                        ),
                        style = MaterialTheme.typography.labelMedium,
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
    onConfirm: (String, Int, String?) -> Unit
) {
    var activity by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

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
                        onConfirm(activity, dur, note.ifBlank { null })
                    }
                },
                enabled = activity.isNotBlank() && (duration.toIntOrNull() ?: 0) > 0
            ) { Text("Log") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}
