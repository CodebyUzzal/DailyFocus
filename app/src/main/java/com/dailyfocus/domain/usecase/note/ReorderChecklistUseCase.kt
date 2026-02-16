package com.dailyfocus.domain.usecase.note

import com.dailyfocus.domain.model.Note
import com.dailyfocus.domain.repository.NoteRepository
import java.time.LocalDateTime
import java.util.Collections
import javax.inject.Inject

class ReorderChecklistUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    // In-memory reorder and save
    suspend operator fun invoke(note: Note, fromIndex: Int, toIndex: Int) {
        if (fromIndex < 0 || toIndex < 0 || fromIndex >= note.items.size || toIndex >= note.items.size) return
        
        val mutableItems = note.items.toMutableList()
        Collections.swap(mutableItems, fromIndex, toIndex)
        
        // Re-index positions to ensure deterministic persistence
        val reindexedItems = mutableItems.mapIndexed { index, item ->
            item.copy(position = index)
        }
        
        val updatedNote = note.copy(
            items = reindexedItems,
            updatedAt = LocalDateTime.now()
        )
        repository.saveNote(updatedNote)
    }
}
