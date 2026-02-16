package com.dailyfocus.domain.usecase.note

import com.dailyfocus.domain.model.Note
import com.dailyfocus.domain.repository.NoteRepository
import java.time.LocalDateTime
import javax.inject.Inject

class SaveNoteUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note) {
        // Auto-update modification time
        val noteToSave = note.copy(updatedAt = LocalDateTime.now())
        repository.saveNote(noteToSave)
    }
}
