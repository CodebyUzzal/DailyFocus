package com.dailyfocus.domain.usecase.note

import com.dailyfocus.domain.model.Note
import com.dailyfocus.domain.repository.NoteRepository
import javax.inject.Inject

class AddNoteUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note): Long {
        if (note.title.isNullOrBlank() && note.content.isNullOrBlank() && note.checklistItems.isEmpty()) {
             // Don't save empty notes, but maybe we should return -1 or throw? 
             // flexible: just return 0 or -1 if invalid, but for now let's allow saving, 
             // maybe UI handles empty state blocking.
        }
        return repository.insertNote(note)
    }
}
