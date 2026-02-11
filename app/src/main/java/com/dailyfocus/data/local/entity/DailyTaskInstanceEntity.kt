package com.dailyfocus.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.dailyfocus.domain.model.TaskCategory
import java.time.LocalDate

@Entity(
    tableName = "daily_task_instances",
    foreignKeys = [
        ForeignKey(
            entity = RoutineEntity::class,
            parentColumns = ["id"],
            childColumns = ["recurringTaskId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["date"]),
        Index(value = ["recurringTaskId"]),
        Index(value = ["recurringTaskId", "date"], unique = true)
    ]
)
data class DailyTaskInstanceEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val recurringTaskId: Long,
    val date: LocalDate,
    val isCompleted: Boolean = false,
    val title: String,
    val category: TaskCategory
)
