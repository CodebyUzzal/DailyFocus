package com.dailyfocus.presentation.habits

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocalFireDepartment
import com.dailyfocus.core.ui.components.DailyFocusScaffold
import com.dailyfocus.core.ui.components.EmptyState
import com.dailyfocus.core.ui.components.PremiumExtendedFAB
import com.dailyfocus.core.ui.theme.Spacing
import com.dailyfocus.presentation.habits.components.HabitCard
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dailyfocus.domain.model.HabitFrequency
import com.dailyfocus.domain.usecase.habits.HabitWithStreak

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitsScreen(
    onNavigateToDetail: (Long) -> Unit,
    viewModel: HabitsViewModel = hiltViewModel()
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
            // Using SectionHeader-like title or just standard large text at top of list
             Box(modifier = Modifier.padding(top = Spacing.l, start = Spacing.m, bottom = Spacing.m)) {
                 Text(
                     text = "Habits", 
                     style = MaterialTheme.typography.headlineLarge,
                     color = MaterialTheme.colorScheme.primary
                 )
             }
        },
        floatingActionButton = {
            PremiumExtendedFAB(
                onClick = { showAddDialog = true },
                text = { Text("New Habit") },
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                containerColor = MaterialTheme.colorScheme.secondary, // Green/Teal for habits
                contentColor = MaterialTheme.colorScheme.onSecondary
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        if (state.habits.isEmpty() && !state.isLoading) {
             Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                EmptyState(
                    message = "Start building a better version of yourself.",
                    subMessage = "Add your first habit.",
                    icon = Icons.Default.LocalFireDepartment
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
                items(state.habits, key = { it.habit.id }) { habitWithStreak ->
                    HabitCard(
                        habitWithStreak = habitWithStreak,
                        onToggleToday = { viewModel.onToggleToday(habitWithStreak.habit.id) },
                        onClick = { onNavigateToDetail(habitWithStreak.habit.id) },
                        modifier = Modifier.padding(horizontal = Spacing.m)
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        AddHabitDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { name, frequency ->
                viewModel.onAddHabit(name, frequency)
                showAddDialog = false
            }
        )
    }
}



@Composable
private fun AddHabitDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, HabitFrequency) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var isDaily by remember { mutableStateOf(true) }
    var daysPerWeek by remember { mutableStateOf("5") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Habit") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Habit name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = isDaily,
                        onClick = { isDaily = true },
                        label = { Text("Daily") }
                    )
                    FilterChip(
                        selected = !isDaily,
                        onClick = { isDaily = false },
                        label = { Text("Custom Weekly") }
                    )
                }
                if (!isDaily) {
                    OutlinedTextField(
                        value = daysPerWeek,
                        onValueChange = { daysPerWeek = it.filter { c -> c.isDigit() } },
                        label = { Text("Days per week (1-7)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (name.isNotBlank()) {
                        val frequency = if (isDaily) HabitFrequency.Daily
                        else HabitFrequency.CustomWeekly(
                            daysPerWeek.toIntOrNull()?.coerceIn(1, 7) ?: 5
                        )
                        onConfirm(name, frequency)
                    }
                },
                enabled = name.isNotBlank()
            ) { Text("Add") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}
