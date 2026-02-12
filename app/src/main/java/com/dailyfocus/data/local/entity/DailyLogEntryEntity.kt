package com.dailyfocus.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(
    tableName = "focus_logs",
    indices = [Index(value = ["date"])]
)
data class DailyLogEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val activityName: String,
    val durationMinutes: Int,
    val type: com.dailyfocus.domain.model.LogType = com.dailyfocus.domain.model.LogType.FOCUS,
    val note: String? = null,
    val date: LocalDate,
    val createdAt: LocalDateTime = LocalDateTime.now()
)
