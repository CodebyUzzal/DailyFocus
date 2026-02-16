package com.dailyfocus.domain.model

data class NoteContent(
    val id: Long = 0,
    val text: String,
    val isChecked: Boolean = false,
    val position: Int
)
