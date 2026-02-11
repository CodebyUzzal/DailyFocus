package com.dailyfocus.presentation.today.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dailyfocus.domain.model.TaskCategory

import com.dailyfocus.core.ui.theme.Spacing

/**
 * Horizontally scrollable filter chips for Personal / Office categories.
 * Null selection means "show all."
 */
@Composable
fun CategoryFilterChips(
    selectedCategory: TaskCategory?,
    onCategorySelected: (TaskCategory?) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.m, vertical = Spacing.xs),
        horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
    ) {
        FilterChip(
            selected = selectedCategory == null,
            onClick = { onCategorySelected(null) },
            label = { Text("All") }
        )
        TaskCategory.entries.forEach { category ->
            FilterChip(
                selected = selectedCategory == category,
                onClick = {
                    onCategorySelected(
                        if (selectedCategory == category) null else category
                    )
                },
                label = {
                    Text(
                        category.name.lowercase()
                            .replaceFirstChar { it.uppercase() }
                    )
                }
            )
        }
    }
}
