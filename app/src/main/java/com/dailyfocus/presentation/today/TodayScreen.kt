package com.dailyfocus.presentation.today

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dailyfocus.presentation.today.components.*
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import com.dailyfocus.presentation.today.components.TodayTaskCard
import androidx.compose.ui.Alignment

/**
 * Main Today tab screen combining daily checklist, today tasks, and category filter.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodayScreen(
    onNavigateToRoutines: () -> Unit,
    viewModel: TodayViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showAddDialog by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }

    // Handle messages
    LaunchedEffect(state.userMessage) {
        state.userMessage?.let { message ->
            snackbarHostState.showSnackbar(message.text)
            viewModel.onMessageDismissed()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Today",
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Text(
                            text = state.date.format(
                                DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
                            ),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToRoutines) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Manage recurring tasks"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add task")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    CategoryFilterChips(
                        selectedCategory = state.categoryFilter,
                        onCategorySelected = viewModel::onCategoryFilterChanged
                    )
                }

                item {
                    HorizontalDivider()
                }

                // ── Routines Section ─────────────────────────────────────────
                if (state.dailyChecklist.isNotEmpty()) {
                    item {
                        Text(
                            text = "Today's Routines",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    items(state.dailyChecklist, key = { "routine_${it.id}" }) { instance ->
                        DailyTaskItem(
                            title = instance.title,
                            isCompleted = instance.isCompleted,
                            onToggle = { viewModel.onToggleRecurringInstance(instance.id, instance.isCompleted) }
                        )
                    }
                }

                // ── One-off Tasks Section ────────────────────────────────────
                if (state.todayTasks.isNotEmpty()) {
                    item {
                        Text(
                            text = "Today Tasks",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                    items(state.todayTasks, key = { "task_${it.task.id}" }) { taskWithItems ->
                        TodayTaskCard(
                            taskWithItems = taskWithItems,
                            onToggleTask = { viewModel.onToggleTodayTask(taskWithItems.task) },
                            onToggleItem = { item -> viewModel.onToggleTaskItem(item, taskWithItems.task) },
                            onAddSubItem = viewModel::onAddTaskItem
                        )
                    }
                }
                
                if (state.dailyChecklist.isEmpty() && state.todayTasks.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier.fillParentMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "All done for today!",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(80.dp)) // FAB clearance
                }
            }
        }
    }

    if (showAddDialog) {
        // This dialog is for One-off tasks only. Routines are managed elsewhere (assumed).
        // Updating title to be specific.
        AddTaskDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { title, category ->
                viewModel.onAddTask(title, category)
                showAddDialog = false
            },
            dialogTitle = "New One-off Task" // Pass the specific title
        )
    }
}
