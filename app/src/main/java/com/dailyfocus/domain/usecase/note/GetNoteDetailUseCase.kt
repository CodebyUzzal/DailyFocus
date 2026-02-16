package com.dailyfocus.domain.usecase.note

import com.dailyfocus.domain.model.Note
import com.dailyfocus.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNoteDetailUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    operator fun invoke(id: Long): Flow<Note?> {
        return repository.getNoteById(id)
    }
}
