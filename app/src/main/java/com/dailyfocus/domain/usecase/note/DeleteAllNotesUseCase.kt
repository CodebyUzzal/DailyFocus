package com.dailyfocus.domain.usecase.note

import com.dailyfocus.domain.repository.NoteRepository

class DeleteAllNotesUseCase(
    private val repository: NoteRepository
) {
    suspend operator fun invoke() {
        repository.deleteAllNotes()
    }
}
