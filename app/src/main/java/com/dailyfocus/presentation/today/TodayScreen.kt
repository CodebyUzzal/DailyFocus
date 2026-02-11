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
import androidx.compose.ui.Alignment
import androidx.compose.material.icons.filled.Check
import com.dailyfocus.core.ui.components.DailyFocusScaffold
import com.dailyfocus.core.ui.components.EmptyState
import com.dailyfocus.core.ui.components.GreetingHeader
import com.dailyfocus.core.ui.components.PremiumExtendedFAB
import com.dailyfocus.core.ui.components.SectionHeader
import com.dailyfocus.core.ui.theme.Spacing
import com.dailyfocus.presentation.today.components.*
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


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

    DailyFocusScaffold(
        topBar = {
            // No standard TopAppBar, using GreetingHeader inside the scrollable content or fixed at top?
            // Plan says "Hero Header (Greeting...)" implying it's part of the scroll or a custom top bar.
            // "Top of Today screen" -> usually scrollable so it disappears when scrolling down?
            // Or fixed? Reference apps usually scroll it.
            // However, GreetingHeader is large.
            // Let's hide the default TopAppBar and put GreetingHeader as the first item in lazy column.
            // BUT we have a "Settings" action. We need to place that somewhere.
            // Maybe a row with Greeting and Settings icon?
            // Or a transparent TopAppBar overlay?
            // Let's implement a custom top row in the scrollable content.
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
                contentPadding = PaddingValues(bottom = Spacing.maximize), // Bottom padding for FAB
                verticalArrangement = Arrangement.spacedBy(Spacing.l) // Wider spacing for premium feel
            ) {
                // ── Header Section ───────────────────────────────────────────
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top // Align top to handle different heights
                    ) {
                        GreetingHeader(
                            date = state.date.format(
                                DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)
                            ),
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = onNavigateToRoutines,
                            modifier = Modifier.padding(top = Spacing.l, end = Spacing.m)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Manage routines",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // ── Summary Card ─────────────────────────────────────────────
                item {
                    Box(modifier = Modifier.padding(horizontal = Spacing.m)) {
                        // Calculate progress (just an example calculation)
                        val totalTasks = state.dailyChecklist.size + state.todayTasks.size
                        val completedTasks = state.dailyChecklist.count { it.isCompleted } + state.todayTasks.count { it.task.isCompleted }
                        val progress = if (totalTasks > 0) completedTasks.toFloat() / totalTasks else 0f
                        
                        SummaryCard(completionPercentage = progress)
                    }
                }

                // ── Filters ──────────────────────────────────────────────────
                item {
                    CategoryFilterChips(
                        selectedCategory = state.categoryFilter,
                        onCategorySelected = viewModel::onCategoryFilterChanged
                    )
                }

                // ── Routines Section ─────────────────────────────────────────
                if (state.dailyChecklist.isNotEmpty()) {
                    item {
                        SectionHeader(
                            title = "Daily Routines",
                            modifier = Modifier.padding(horizontal = Spacing.m)
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
                        SectionHeader(
                            title = "Priorities",
                            modifier = Modifier.padding(horizontal = Spacing.m),
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
                } else if (state.dailyChecklist.isEmpty()) {
                    // Empty State if BOTH are empty
                     item {
                        EmptyState(
                            message = "You're all set for today.",
                            subMessage = "Take a breath or add a new task.",
                            icon = Icons.Default.Check
                        )
                    }
                }

                 item {
                    Spacer(modifier = Modifier.height(Spacing.maximize)) // Fab clearance
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
