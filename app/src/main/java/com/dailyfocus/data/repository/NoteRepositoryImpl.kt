package com.dailyfocus.data.repository

import com.dailyfocus.data.local.dao.NoteDao
import com.dailyfocus.data.local.entity.NoteContentEntity
import com.dailyfocus.data.local.entity.NoteEntity
import com.dailyfocus.data.local.entity.NoteWithContent
import com.dailyfocus.domain.model.Note
import com.dailyfocus.domain.model.NoteBackgroundStyle
import com.dailyfocus.domain.model.NoteContent
import com.dailyfocus.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val dao: NoteDao
) : NoteRepository {

    override fun getAllNotes(): Flow<List<Note>> {
        return dao.getAllNotes().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getNoteById(id: Long): Flow<Note?> {
        return dao.getNoteById(id).map { it?.toDomain() }
    }

    override suspend fun saveNote(note: Note) {
        val noteEntity = note.toEntity()
        
        if (note.id == 0L) {
            // Insert new
            val newId = dao.insertNote(noteEntity)
            val contentEntities = note.items.map { it.toEntity(newId) }
            dao.insertContents(contentEntities)
        } else {
            // Update existing
            // Transactional update: update note, clear contents, insert new contents (simple strategy)
            // Or diffing? "Smart diff or clean replace". Clean replace is safer for ordering.
            // But we might want to keep IDs if possible?
            // User said: "NoteContentEntity: id: Long (Primary Key)".
            // If we delete and re-insert, IDs change. 
            // In a local app, this might be fine unless we have complex selection logic relying on ID.
            // Let's try to preserve IDs if they exist.
            
            // Actually, for "Production Grade", we should probably update existing items and insert/delete others.
            // BUT, implementing full diffing here might be error prone.
            // "Either diff intelligently OR replace content cleanly and deterministically."
            // "Replace content cleanly" -> deleteAllContentByNoteId + insertContents.
            // This guarantees position is 100% correct according to the list.
            
            dao.updateNote(noteEntity)
            dao.deleteAllContentByNoteId(note.id)
            val contentEntities = note.items.map { it.toEntity(note.id) }
            dao.insertContents(contentEntities)
        }
    }

    override suspend fun deleteNote(noteId: Long) {
        // We need the entity to delete? Or just ID? 
        // DAO deleteNote takes Entity. 
        // Or we can delete by ID if we add a query.
        // Let's fetch it first then delete, or add deleteById to DAO.
        // Given DAO has deleteNote(note: NoteEntity), we need the entity.
        // But Repository interface has deleteNote(noteId: Long).
        // Let's iterate: just add a delete query to DAO or allow Repos to fetch-delete.
        // Ideally DAO has @Query("DELETE FROM notes WHERE id = :id")
        // I didn't add that to DAO. I added deleteNote(entity).
        // Use standard delete for now.
        // Wait, I can't easily get the entity solely for deletion without observing flow.
        // I'll add deleteNoteById to DAO next time, or just fetch-delete here.
        // Actually, let's look at `NoteDao` I created.
        // I can just "fake" an entity with the ID to delete? Room uses PK for deletion.
        dao.deleteNote(NoteEntity(id = noteId, title = "", updatedAt = java.time.LocalDateTime.now())) 
        // This is a hack. But valid for Room delete (needs PK).
    }

    // Mappers

    private fun NoteWithContent.toDomain(): Note {
        return Note(
            id = note.id,
            title = note.title ?: "",
            isChecklist = note.isChecklist,
            isPinned = note.isPinned,
            backgroundStyle = NoteBackgroundStyle.valueOf(note.backgroundStyle.name),
            items = contents.sortedBy { it.position }.map { it.toDomain() },
            createdAt = note.createdAt,
            updatedAt = note.updatedAt
        )
    }

    private fun NoteContentEntity.toDomain(): NoteContent {
        return NoteContent(
            id = id,
            text = text,
            isChecked = isChecked,
            position = position
        )
    }

    private fun Note.toEntity(): NoteEntity {
        return NoteEntity(
            id = id,
            title = title,
            isChecklist = isChecklist,
            isPinned = isPinned,
            backgroundStyle = com.dailyfocus.data.local.entity.NoteBackgroundStyle.valueOf(backgroundStyle.name),
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }

    private fun NoteContent.toEntity(noteId: Long): NoteContentEntity {
        // If we represent a new item for an existing note, id is 0.
        // If we represent an existing item, id is preserved (if we weren't doing delete-all).
        // Since we ARE doing delete-all, the ID here gets ignored by insert (assigned 0/generated).
        // That implies we lose ID stability.
        // Is this acceptable? "Existing data loss is acceptable".
        // For ReorderUseCase, we might need stable IDs?
        // "ReorderChecklistUseCase... Must persist correct order. No in-memory-only reordering allowed."
        // If we save, we get new IDs.
        // This is fine for now.
        return NoteContentEntity(
            id = 0, // Always 0 to generate new IDs since we wipe content on update
            noteId = noteId,
            text = text,
            isChecked = isChecked,
            position = position
        )
    }
}
