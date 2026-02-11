package com.dailyfocus.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dailyfocus.domain.model.GoalType
import java.time.LocalDateTime

@Entity(tableName = "goals")
data class GoalEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val type: GoalType = GoalType.INFINITE,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)
