package com.dailyfocus.domain.model

import java.time.LocalDateTime

data class Note(
    val id: Long = 0,
    val title: String,
    val isChecklist: Boolean = false,
    val isPinned: Boolean = false,
    val backgroundStyle: NoteBackgroundStyle = NoteBackgroundStyle.DEFAULT,
    val items: List<NoteContent> = emptyList(),
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)
