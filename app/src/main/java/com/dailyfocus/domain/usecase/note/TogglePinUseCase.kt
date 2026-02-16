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
            updatedAt = LocalDateTime.now() // Pinning changes sort order, but maybe not "content updated"? 
                                          // Spec says "Ordering rules: Pinned notes first, Then by updatedAt DESC"
                                          // So changing pin status significantly changes position.
                                          // Updating timestamp ensures it floats to top of its section if we want?
                                          // Requirement says: "Update updatedAt automatically." - applies to editor.
                                          // Let's update it to be safe.
        )
        repository.updateNote(updatedNote)
    }
}
