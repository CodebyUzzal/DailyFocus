package com.dailyfocus.presentation.today

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dailyfocus.core.ui.components.*
import com.dailyfocus.core.ui.theme.Spacing
import com.dailyfocus.domain.model.TaskCategory
import com.dailyfocus.presentation.today.components.*
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodayScreen(
    onNavigateToRoutines: () -> Unit,
    onOpenSettings: () -> Unit,
    onNavigateToAbout: () -> Unit,
    viewModel: TodayViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showAddDialog by remember { mutableStateOf(false) }
    
    // Collapsible state for completed tasks
    var isCompletedExpanded by remember { mutableStateOf(false) }
    
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.userMessage) {
        state.userMessage?.let { message ->
            snackbarHostState.showSnackbar(message.text)
            viewModel.onMessageDismissed()
        }
    }

    DailyFocusScaffold(
        topBar = { /* Header is inside LazyColumn */ },
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
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(padding)
        ) {
            if (state.isLoading) {
                 Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                     CircularProgressIndicator()
                 }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 96.dp), // Check implementation plan
                    verticalArrangement = Arrangement.spacedBy(Spacing.m) // Increased to 16dp
                ) {
                    // ── Header ──────────────────────────────────────────────
                    item {
                         val totalTasks = state.todayTasks.size
                         val completedTasks = state.todayTasks.count { it.isCompleted }
                         val progress = if (totalTasks > 0) completedTasks.toFloat() / totalTasks else 0f
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            TodayHeader(
                                username = state.userName,
                                date = state.date.format(
                                    DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
                                ),
                                progress = progress,
                                taskCount = totalTasks,
                                completedCount = completedTasks,
                                focusMinutes = state.todayFocusMinutes,
                                modifier = Modifier.weight(1f)
                            )
                            
                            // Actions
                             Row(
                                 verticalAlignment = Alignment.CenterVertically, 
                                 modifier = Modifier.padding(top = Spacing.l, end = Spacing.s)
                             ) {
                                IconButton(onClick = onNavigateToRoutines) {
                                    Icon(
                                        imageVector = Icons.Default.DateRange,
                                        contentDescription = "Manage routines",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Box {
                                    var showMenu by remember { mutableStateOf(false) }
                                    IconButton(onClick = { showMenu = true }) {
                                        Icon(
                                            imageVector = Icons.Default.MoreVert,
                                            contentDescription = "More",
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    DropdownMenu(
                                        expanded = showMenu,
                                        onDismissRequest = { showMenu = false }
                                    ) {
                                        DropdownMenuItem(
                                            text = { Text("Settings") },
                                            onClick = { showMenu = false; onOpenSettings() }
                                        )
                                        DropdownMenuItem(
                                            text = { Text("About") },
                                            onClick = { showMenu = false; onNavigateToAbout() }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // ── Filters ─────────────────────────────────────────────
                    item {
                        CategoryFilterChips(
                            selectedCategory = state.categoryFilter,
                            onCategorySelected = viewModel::onCategoryFilterChanged
                        )
                    }

                    // ── Active Tasks ──────────────────────────────────────
                    item {
                        SectionHeader(
                            title = "Today",
                            modifier = Modifier.padding(horizontal = Spacing.m)
                        )
                    }

                    val activeTasks = state.todayTasks.filter { !it.isCompleted }
                    if (activeTasks.isNotEmpty()) {
                        items(activeTasks, key = { "task_${it.id}" }) { task ->
                            DailyFocusTaskItem(
                                title = task.title,
                                isCompleted = task.isCompleted,
                                category = task.category,
                                isRecurring = task.recurringTaskId != null,
                                note = task.note,
                                showCategory = state.categoryFilter == null,
                                onToggle = { viewModel.onToggleTodayTask(task) },
                                modifier = Modifier.padding(horizontal = Spacing.m, vertical = 4.dp)
                            )
                        }
                    } else if (state.todayTasks.isEmpty()) {
                        item {
                            PremiumEmptyState(
                                onAddFirstTask = { showAddDialog = true }
                            )
                        }
                    }

                    // ── Completed Tasks (Collapsible) ─────────────────────
                    val completedTasksList = state.todayTasks.filter { it.isCompleted }
                    if (completedTasksList.isNotEmpty()) {
                         item {
                            Spacer(modifier = Modifier.height(Spacing.m))
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = Spacing.m),
                                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                            )
                            Spacer(modifier = Modifier.height(Spacing.s))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { isCompletedExpanded = !isCompletedExpanded }
                                    .padding(horizontal = Spacing.m, vertical = Spacing.s),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Completed • ${completedTasksList.size}",
                                    style = MaterialTheme.typography.titleSmall, // Smaller, less intrusive
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontWeight = FontWeight.Medium
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                val rotation by animateFloatAsState(if (isCompletedExpanded) 180f else 0f)
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = if (isCompletedExpanded) "Collapse" else "Expand",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.rotate(rotation)
                                )
                            }
                        }
                        
                         if (isCompletedExpanded) {
                             items(completedTasksList, key = { "task_${it.id}" }) { task ->
                                DailyFocusTaskItem(
                                    title = task.title,
                                    isCompleted = task.isCompleted,
                                    category = task.category,
                                    isRecurring = task.recurringTaskId != null,
                                    note = task.note,
                                    showCategory = state.categoryFilter == null,
                                    onToggle = { viewModel.onToggleTodayTask(task) },
                                    modifier = Modifier
                                        .padding(start = Spacing.xl, end = Spacing.m, top = 4.dp, bottom = 4.dp) // Indented
                                        .alpha(0.7f) // Reduced opacity
                                    // .animateItemPlacement() removed for build stability 
                                )
                            }
                        }
                    }
                }
                
                // Bottom Scroll Overlay
                ScrollOverlay(
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }
    }

    if (showAddDialog) {
        AddTaskDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { title, category, note ->
                viewModel.onAddTask(title, category, note)
                showAddDialog = false
            },
            defaultCategory = state.categoryFilter ?: TaskCategory.PERSONAL,
            dialogTitle = "New Focus"
        )
    }
}
