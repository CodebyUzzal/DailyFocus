package com.dailyfocus.presentation.today.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dailyfocus.domain.model.TaskCategory
import com.dailyfocus.domain.model.TodayTask

/**
 * Bottom sheet dialog for adding or editing a task.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskEditorDialog(
    task: TodayTask? = null,
    onDismiss: () -> Unit,
    onConfirm: (title: String, category: TaskCategory, note: String?) -> Unit,
    defaultCategory: TaskCategory = TaskCategory.PERSONAL
) {
    var title by remember { mutableStateOf(task?.title ?: "") }
    var note by remember { mutableStateOf(task?.note ?: "") }
    var selectedCategory by remember { mutableStateOf(task?.category ?: defaultCategory) }

    val isEditing = task != null
    val dialogTitle = if (isEditing) "Edit Focus" else "New Focus"
    val buttonText = if (isEditing) "Save" else "Add"

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
                style = MaterialTheme.typography.titleLarge
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Task title") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("Note (optional)") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3
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
                            onConfirm(title, selectedCategory, note.ifBlank { null })
                        }
                    },
                    enabled = title.isNotBlank()
                ) {
                    Text(buttonText)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
