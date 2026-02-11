package com.dailyfocus.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dailyfocus.domain.model.TaskCategory
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(
    tableName = "recurring_tasks",
    indices = [androidx.room.Index(value = ["lastCompletedDate"])]
)
data class RoutineEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val category: TaskCategory,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val lastCompletedDate: LocalDate? = null,
    // kept for migration compatibility if needed, though we moved away from 'isStreakEnabled' logic
    val isStreakEnabled: Boolean = true, 
    val reminderTime: String? = null,
    val isActive: Boolean = true,
    val position: Int = 0,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)
