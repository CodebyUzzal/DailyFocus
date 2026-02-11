package com.dailyfocus.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dailyfocus.domain.model.HabitFrequency
import java.time.LocalDateTime

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val frequency: HabitFrequency = HabitFrequency.Daily,
    val isActive: Boolean = true,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)
