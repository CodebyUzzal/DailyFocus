package com.dailyfocus.domain.model

import java.time.LocalDateTime

data class Note(
    val id: Long = 0,
    val title: String?,
    val content: String?,
    val isChecklist: Boolean = false,
    val checklistItems: List<ChecklistItem> = emptyList(),
    val color: NoteColor = NoteColor.DEFAULT,
    val isPinned: Boolean = false,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)

data class ChecklistItem(
    val text: String,
    val isChecked: Boolean = false
)

enum class NoteColor {
    DEFAULT,
    RED,
    ORANGE,
    YELLOW,
    GREEN,
    BLUE,
    PURPLE,
    PINK,
    BROWN,
    GRAY
}
