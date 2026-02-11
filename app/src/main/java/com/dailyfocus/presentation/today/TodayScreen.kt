package com.dailyfocus.presentation.today

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dailyfocus.core.ui.components.DailyFocusScaffold
import com.dailyfocus.core.ui.components.EmptyState
import com.dailyfocus.core.ui.components.GreetingHeader
import com.dailyfocus.core.ui.components.PremiumExtendedFAB
import com.dailyfocus.core.ui.components.SectionHeader
import com.dailyfocus.core.ui.theme.Spacing
import com.dailyfocus.domain.model.TaskCategory
import com.dailyfocus.presentation.today.components.*
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

/**
 * Main Today tab screen — unified task list with category filtering.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodayScreen(
    onNavigateToRoutines: () -> Unit,
    onOpenSettings: () -> Unit,
    viewModel: TodayViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showAddDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.userMessage) {
        state.userMessage?.let { message ->
            snackbarHostState.showSnackbar(message.text)
            viewModel.onMessageDismissed()
        }
    }

    DailyFocusScaffold(
        topBar = { /* GreetingHeader is inside the LazyColumn */ },
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
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(bottom = Spacing.maximize),
                verticalArrangement = Arrangement.spacedBy(Spacing.l)
            ) {
                // ── Header ──────────────────────────────────────────────
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        GreetingHeader(
                            username = state.userName,
                            date = state.date.format(
                                DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)
                            ),
                            modifier = Modifier.weight(1f)
                        )
                        Row(modifier = Modifier.padding(top = Spacing.l, end = Spacing.m)) {
                             // Routines Button
                            IconButton(onClick = onNavigateToRoutines) {
                                Icon(
                                    imageVector = androidx.compose.material.icons.Icons.Default.DateRange,
                                    contentDescription = "Manage recurring tasks",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            // Settings Button
                            IconButton(onClick = onOpenSettings) {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = "Settings",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                // ── Summary Card ────────────────────────────────────────
                item {
                    Box(modifier = Modifier.padding(horizontal = Spacing.m)) {
                        val totalTasks = state.todayTasks.size
                        val completedTasks = state.todayTasks.count { it.isCompleted }
                        val progress = if (totalTasks > 0) completedTasks.toFloat() / totalTasks else 0f
                        SummaryCard(completionPercentage = progress)
                    }
                }

                // ── Filters ─────────────────────────────────────────────
                item {
                    CategoryFilterChips(
                        selectedCategory = state.categoryFilter,
                        onCategorySelected = viewModel::onCategoryFilterChanged
                    )
                }

                // ── Tasks ───────────────────────────────────────────────
                if (state.todayTasks.isNotEmpty()) {
                    item {
                        SectionHeader(
                            title = "Today's Tasks",
                            modifier = Modifier.padding(horizontal = Spacing.m)
                        )
                    }
                    items(state.todayTasks, key = { "task_${it.id}" }) { task ->
                        DailyTaskItem(
                            title = task.title,
                            isCompleted = task.isCompleted,
                            category = task.category,
                            isRecurring = task.recurringTaskId != null,
                            onToggle = { viewModel.onToggleTodayTask(task) }
                        )
                    }
                } else {
                    item {
                        EmptyState(
                            message = "You're all set for today.",
                            subMessage = "Take a breath or add a new task.",
                            icon = Icons.Default.Check
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(Spacing.maximize))
                }
            }
        }
    }

    if (showAddDialog) {
        AddTaskDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { title, category ->
                viewModel.onAddTask(title, category)
                showAddDialog = false
            },
            defaultCategory = state.categoryFilter ?: TaskCategory.PERSONAL,
            dialogTitle = "New Task"
        )
    }
}
