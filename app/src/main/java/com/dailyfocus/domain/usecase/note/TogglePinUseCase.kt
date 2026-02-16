package com.dailyfocus.domain.usecase.note

import com.dailyfocus.domain.model.Note
import com.dailyfocus.domain.repository.NoteRepository
import java.time.LocalDateTime
import javax.inject.Inject

class TogglePinUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note) {
        val updatedNote = note.copy(
            isPinned = !note.isPinned,
            updatedAt = LocalDateTime.now()
        )
        repository.saveNote(updatedNote)
    }
}
