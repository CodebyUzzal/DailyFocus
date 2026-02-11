package com.dailyfocus.presentation.today.routines

import androidx.compose.foundation.clickable
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
import com.dailyfocus.core.ui.theme.AppShapes
import com.dailyfocus.core.ui.theme.Elevation
import com.dailyfocus.core.ui.theme.Spacing
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dailyfocus.domain.model.Routine
import com.dailyfocus.domain.model.TaskCategory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineListScreen(
    onNavigateBack: () -> Unit,
    viewModel: RoutineViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showAddDialog by remember { mutableStateOf(false) }
    var routineToEdit by remember { mutableStateOf<Routine?>(null) }
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
                         text = "Manage Routines", 
                         style = MaterialTheme.typography.headlineMedium,
                         color = MaterialTheme.colorScheme.primary
                     )
                 }
             }
        },
        floatingActionButton = {
            PremiumExtendedFAB(
                onClick = { showAddDialog = true },
                text = { Text("Add Routine") },
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
                    message = "No routines yet.",
                    subMessage = "Create a routine to simplify your day.",
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
                items(state.routines, key = { it.id }) { routine ->
                    RoutineItemCard(
                        routine = routine,
                        onToggleActive = { viewModel.onToggleActive(routine) },
                        onEdit = { routineToEdit = routine },
                        onDelete = { viewModel.onDeleteRoutine(routine.id) }
                    )
                }
            }
        }
    }

    if (showAddDialog || routineToEdit != null) {
        AddEditRoutineDialog(
            routineToEdit = routineToEdit,
            onDismiss = {
                showAddDialog = false
                routineToEdit = null
            },
            onConfirm = { title, category ->
                if (routineToEdit != null) {
                    viewModel.onUpdateRoutine(routineToEdit!!.copy(title = title, category = category))
                } else {
                    viewModel.onAddRoutine(title, category)
                }
                showAddDialog = false
                routineToEdit = null
            }
        )
    }
}

@Composable
fun RoutineItemCard(
    routine: Routine,
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
                    text = routine.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = routine.category.name.lowercase().replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Switch(
                checked = routine.isActive,
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

@Composable
fun AddEditRoutineDialog(
    routineToEdit: Routine?,
    onDismiss: () -> Unit,
    onConfirm: (String, TaskCategory) -> Unit
) {
    var title by remember { mutableStateOf(routineToEdit?.title ?: "") }
    var selectedCategory by remember { mutableStateOf(routineToEdit?.category ?: TaskCategory.PERSONAL) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (routineToEdit == null) "New Routine" else "Edit Routine") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Routine Title") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TaskCategory.entries.forEach { category ->
                        FilterChip(
                            selected = selectedCategory == category,
                            onClick = { selectedCategory = category },
                            label = { Text(category.name.lowercase().replaceFirstChar { it.uppercase() }) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (title.isNotBlank()) {
                        onConfirm(title, selectedCategory)
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
