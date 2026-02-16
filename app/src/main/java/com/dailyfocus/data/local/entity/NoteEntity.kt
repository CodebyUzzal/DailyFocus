package com.dailyfocus.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String?,
    val isChecklist: Boolean = false,
    val isPinned: Boolean = false,
    val backgroundStyle: NoteBackgroundStyle = NoteBackgroundStyle.DEFAULT,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)

enum class NoteBackgroundStyle {
    DEFAULT,
    RED,
    ORANGE,
    YELLOW,
    GREEN,
    BLUE,
    PURPLE,
    PINK,
    BROWN,
    GRAY,
    // Add more styles like gradients or textures later
}
