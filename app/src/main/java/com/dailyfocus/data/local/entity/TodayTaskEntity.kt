package com.dailyfocus.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.dailyfocus.domain.model.TaskCategory
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(
    tableName = "today_tasks",
    indices = [Index(value = ["date"])]
)
data class TodayTaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val category: TaskCategory,
    val date: LocalDate,
    val isCompleted: Boolean = false,
    val recurringTaskId: Long? = null,
    val note: String? = null,
    val createdAt: LocalDateTime = LocalDateTime.now()
)
