package com.dailyfocus.presentation.goals

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.Flag
import com.dailyfocus.core.ui.components.DailyFocusScaffold
import com.dailyfocus.core.ui.components.EmptyState
import com.dailyfocus.core.ui.components.PremiumExtendedFAB
import com.dailyfocus.core.ui.theme.Spacing
import com.dailyfocus.presentation.goals.components.GoalCard
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dailyfocus.domain.model.GoalItem
import com.dailyfocus.domain.model.GoalType
import com.dailyfocus.domain.usecase.goals.GoalWithProgress

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalsScreen(
    viewModel: GoalsViewModel = hiltViewModel()
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

    DailyFocusScaffold(
        topBar = {
             Box(modifier = Modifier.padding(top = Spacing.l, start = Spacing.m, bottom = Spacing.m)) {
                 Text(
                     text = "Goals", 
                     style = MaterialTheme.typography.headlineLarge,
                     color = MaterialTheme.colorScheme.primary
                 )
             }
        },
        floatingActionButton = {
            PremiumExtendedFAB(
                onClick = { showAddDialog = true },
                text = { Text("New Goal") },
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                containerColor = MaterialTheme.colorScheme.tertiary, // Different color for Goals
                contentColor = MaterialTheme.colorScheme.onTertiary
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        if (state.goals.isEmpty() && !state.isLoading) {
             Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                EmptyState(
                    message = "Dream big. Start small.",
                    subMessage = "Create your first goal.",
                    icon = Icons.Default.Flag
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(bottom = Spacing.maximize),
                verticalArrangement = Arrangement.spacedBy(Spacing.l) // Wider spacing for goals
            ) {
                items(state.goals, key = { it.goal.id }) { goalWithProgress ->
                    GoalCard(
                        goalWithProgress = goalWithProgress,
                        onToggleItem = { viewModel.onToggleGoalItem(it) },
                        onAddItem = { title -> viewModel.onAddGoalItem(goalWithProgress.goal.id, title) },
                        onDeleteGoal = { viewModel.onDeleteGoal(goalWithProgress.goal.id) },
                        modifier = Modifier.padding(horizontal = Spacing.m)
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        AddGoalDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { title, type ->
                viewModel.onAddGoal(title, type)
                showAddDialog = false
            }
        )
    }
}



@Composable
private fun AddGoalDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, GoalType) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(GoalType.MONTHLY) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Goal") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Goal title") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Text("Type", style = MaterialTheme.typography.labelLarge)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    GoalType.entries.forEach { type ->
                        FilterChip(
                            selected = selectedType == type,
                            onClick = { selectedType = type },
                            label = { Text(type.name.lowercase().replaceFirstChar { it.uppercase() }) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { if (title.isNotBlank()) onConfirm(title, selectedType) },
                enabled = title.isNotBlank()
            ) { Text("Create") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}
