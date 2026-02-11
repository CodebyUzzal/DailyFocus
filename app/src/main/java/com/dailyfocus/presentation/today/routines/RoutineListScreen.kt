package com.dailyfocus.presentation.today.routines

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dailyfocus.core.ui.components.DailyFocusCard
import com.dailyfocus.core.ui.components.DailyFocusScaffold
import com.dailyfocus.core.ui.components.EmptyState
import com.dailyfocus.core.ui.components.PremiumExtendedFAB
import com.dailyfocus.core.ui.theme.Elevation
import com.dailyfocus.core.ui.theme.Spacing
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dailyfocus.domain.model.RecurringTask
import com.dailyfocus.domain.model.TaskCategory
import java.time.DayOfWeek

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineListScreen(
    onNavigateBack: () -> Unit,
    viewModel: RoutineViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showAddDialog by remember { mutableStateOf(false) }
    var routineToEdit by remember { mutableStateOf<RecurringTask?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.userMessage) {
        state.userMessage?.let {
            snackbarHostState.showSnackbar(it.text)
            viewModel.onMessageDismissed()
        }
    }

    DailyFocusScaffold(
        topBar = {
             Box(modifier = Modifier.padding(top = Spacing.l, start = Spacing.m, bottom = Spacing.m)) {
                 Row(verticalAlignment = Alignment.CenterVertically) {
                     IconButton(onClick = onNavigateBack) {
                         Icon(Icons.Default.ArrowBack, "Back", tint = MaterialTheme.colorScheme.onSurface)
                     }
                     Text(
                         text = "Manage Recurring Tasks",
                         style = MaterialTheme.typography.headlineMedium,
                         color = MaterialTheme.colorScheme.primary
                     )
                 }
             }
        },
        floatingActionButton = {
            PremiumExtendedFAB(
                onClick = { showAddDialog = true },
                text = { Text("Add Task") },
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                containerColor = MaterialTheme.colorScheme.primary
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        if (state.routines.isEmpty() && !state.isLoading) {
             Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                EmptyState(
                    message = "No recurring tasks yet.",
                    subMessage = "Create a recurring task to automate your day.",
                    icon = Icons.Default.Edit
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
                items(state.routines, key = { it.id }) { task ->
                    RecurringTaskItemCard(
                        task = task,
                        onToggleActive = { viewModel.onToggleActive(task) },
                        onEdit = { routineToEdit = task },
                        onDelete = { viewModel.onDeleteRoutine(task.id) }
                    )
                }
            }
        }
    }

    if (showAddDialog || routineToEdit != null) {
        AddEditRecurringTaskDialog(
            taskToEdit = routineToEdit,
            onDismiss = {
                showAddDialog = false
                routineToEdit = null
            },
            onConfirm = { title, category, daysOfWeek ->
                if (routineToEdit != null) {
                    viewModel.onUpdateRoutine(
                        routineToEdit!!.copy(title = title, category = category, daysOfWeek = daysOfWeek)
                    )
                } else {
                    viewModel.onAddRoutine(title, category, daysOfWeek)
                }
                showAddDialog = false
                routineToEdit = null
            }
        )
    }
}

@Composable
fun RecurringTaskItemCard(
    task: RecurringTask,
    onToggleActive: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    DailyFocusCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.m),
        elevation = Elevation.Level1
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.m),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text(
                        text = task.category.name.lowercase().replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (task.daysOfWeek.isNotEmpty()) {
                        Text(
                            text = "• ${task.daysOfWeek.joinToString(", ") { it.name.take(3) }}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            Switch(
                checked = task.isActive,
                onCheckedChange = { onToggleActive() }
            )
            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, "Edit", tint = MaterialTheme.colorScheme.primary)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, "Delete", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddEditRecurringTaskDialog(
    taskToEdit: RecurringTask?,
    onDismiss: () -> Unit,
    onConfirm: (String, TaskCategory, Set<DayOfWeek>) -> Unit
) {
    var title by remember { mutableStateOf(taskToEdit?.title ?: "") }
    var selectedCategory by remember { mutableStateOf(taskToEdit?.category ?: TaskCategory.PERSONAL) }
    var selectedDays by remember { mutableStateOf(taskToEdit?.daysOfWeek ?: emptySet()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (taskToEdit == null) "New Recurring Task" else "Edit Recurring Task") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Task Title") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                // Category selection
                Text("Category", style = MaterialTheme.typography.labelMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TaskCategory.entries.forEach { category ->
                        FilterChip(
                            selected = selectedCategory == category,
                            onClick = { selectedCategory = category },
                            label = { Text(category.name.lowercase().replaceFirstChar { it.uppercase() }) }
                        )
                    }
                }
                // Day selection
                Text("Active Days (empty = every day)", style = MaterialTheme.typography.labelMedium)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    DayOfWeek.entries.forEach { day ->
                        FilterChip(
                            selected = day in selectedDays,
                            onClick = {
                                selectedDays = if (day in selectedDays) {
                                    selectedDays - day
                                } else {
                                    selectedDays + day
                                }
                            },
                            label = { Text(day.name.take(3)) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (title.isNotBlank()) {
                        onConfirm(title, selectedCategory, selectedDays)
                    }
                },
                enabled = title.isNotBlank()
            ) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
