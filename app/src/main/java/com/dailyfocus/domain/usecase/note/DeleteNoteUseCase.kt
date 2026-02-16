package com.dailyfocus.domain.usecase.note

import com.dailyfocus.domain.model.Note
import com.dailyfocus.domain.repository.NoteRepository
import javax.inject.Inject

class DeleteNoteUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note) {
        repository.deleteNote(note.id)
    }
}
