package com.dailyfocus.presentation.today.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dailyfocus.domain.model.TaskCategory

/**
 * Bottom sheet dialog for adding a new task to today.
 * [defaultCategory] fixes the category bug — defaults to the active tab's category.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskDialog(
    onDismiss: () -> Unit,
    onConfirm: (title: String, category: TaskCategory) -> Unit,
    defaultCategory: TaskCategory = TaskCategory.PERSONAL,
    dialogTitle: String = "Add Task"
) {
    var title by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(defaultCategory) }

    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text(
                text = dialogTitle,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Task title") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Category",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TaskCategory.entries.forEach { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { selectedCategory = category },
                        label = {
                            Text(category.name.lowercase().replaceFirstChar { it.uppercase() })
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        if (title.isNotBlank()) {
                            onConfirm(title, selectedCategory)
                        }
                    },
                    enabled = title.isNotBlank()
                ) {
                    Text("Add")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
