package com.dailyfocus.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.DayOfWeek

@Composable
fun DaySelectionRow(
    selectedDays: Set<DayOfWeek>,
    onDayToggle: (DayOfWeek) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        DayOfWeek.entries.forEach { day ->
            val isSelected = day in selectedDays
            FilterChip(
                selected = isSelected,
                onClick = { onDayToggle(day) },
                label = { Text(day.name.take(1)) }, // M, T, W...
                modifier = Modifier.weight(1f)
            )
        }
    }
}
