package com.dailyfocus.data.local.dao

import androidx.room.*
import com.dailyfocus.data.local.entity.NoteContentEntity
import com.dailyfocus.data.local.entity.NoteEntity
import com.dailyfocus.data.local.entity.NoteWithContent
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Transaction
    @Query("SELECT * FROM notes ORDER BY isPinned DESC, updatedAt DESC")
    fun getAllNotes(): Flow<List<NoteWithContent>>

    @Transaction
    @Query("SELECT * FROM notes WHERE id = :id")
    fun getNoteById(id: Long): Flow<NoteWithContent?>

    // ── NoteEntity Operations ───────────────────────────────────────────

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity): Long

    @Update
    suspend fun updateNote(note: NoteEntity)

    @Delete
    suspend fun deleteNote(note: NoteEntity)

    // ── NoteContentEntity Operations ────────────────────────────────────

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContent(content: NoteContentEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContents(contents: List<NoteContentEntity>)

    @Update
    suspend fun updateContent(content: NoteContentEntity)
    
    @Update
    suspend fun updateContents(contents: List<NoteContentEntity>)

    @Delete
    suspend fun deleteContent(content: NoteContentEntity)
    
    @Delete
    suspend fun deleteContents(contents: List<NoteContentEntity>)

    @Query("DELETE FROM note_contents WHERE noteId = :noteId")
    suspend fun deleteAllContentByNoteId(noteId: Long)
    
    @Query("DELETE FROM notes")
    suspend fun deleteAllNotes()
}
