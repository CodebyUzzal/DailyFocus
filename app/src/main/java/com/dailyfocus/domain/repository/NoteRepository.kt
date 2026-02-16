package com.dailyfocus.domain.repository

import com.dailyfocus.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getAllNotes(): Flow<List<Note>>
    fun getNoteById(id: Long): Flow<Note?>
    suspend fun saveNote(note: Note)
    suspend fun deleteNote(noteId: Long)
}
