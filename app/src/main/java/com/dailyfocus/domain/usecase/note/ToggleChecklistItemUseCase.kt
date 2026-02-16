package com.dailyfocus.domain.usecase.note

import com.dailyfocus.domain.model.Note
import com.dailyfocus.domain.repository.NoteRepository
import java.time.LocalDateTime
import javax.inject.Inject

class ToggleChecklistItemUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note, itemIndex: Int) {
        if (itemIndex < 0 || itemIndex >= note.items.size) return

        val currentItems = note.items.toMutableList()
        val item = currentItems[itemIndex]
        currentItems[itemIndex] = item.copy(isChecked = !item.isChecked)

        val updatedNote = note.copy(
            items = currentItems,
            updatedAt = LocalDateTime.now() // Ensure we update timestamp
        )
        repository.saveNote(updatedNote)
    }
}
