package com.dailyfocus.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dailyfocus.domain.model.TaskCategory
import java.time.DayOfWeek
import java.time.LocalDateTime

@Entity(tableName = "recurring_tasks")
data class RecurringTaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val category: TaskCategory,
    val daysOfWeek: Set<DayOfWeek> = emptySet(),
    val isActive: Boolean = true,
    val createdAt: LocalDateTime = LocalDateTime.now()
)
