package com.dailyfocus.domain.usecase.note

import com.dailyfocus.domain.model.Note
import com.dailyfocus.domain.repository.NoteRepository
import java.time.LocalDateTime
import javax.inject.Inject

class ToggleChecklistItemUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note, itemIndex: Int) {
        if (itemIndex < 0 || itemIndex >= note.checklistItems.size) return

        val currentItems = note.checklistItems.toMutableList()
        val item = currentItems[itemIndex]
        currentItems[itemIndex] = item.copy(isChecked = !item.isChecked)

        val updatedNote = note.copy(
            checklistItems = currentItems,
            updatedAt = LocalDateTime.now()
        )
        repository.updateNote(updatedNote)
    }
}
