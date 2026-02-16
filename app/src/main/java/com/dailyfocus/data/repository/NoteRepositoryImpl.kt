package com.dailyfocus.data.repository

import com.dailyfocus.data.local.dao.NoteDao
import com.dailyfocus.data.local.entity.NoteEntity
import com.dailyfocus.domain.model.ChecklistItem
import com.dailyfocus.domain.model.Note
import com.dailyfocus.domain.model.NoteColor
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

    override suspend fun insertNote(note: Note): Long {
        return dao.insertNote(note.toEntity())
    }

    override suspend fun updateNote(note: Note) {
        dao.updateNote(note.toEntity())
    }

    override suspend fun deleteNote(note: Note) {
        dao.deleteNote(note.toEntity())
    }

    // Mappers

    private fun NoteEntity.toDomain(): Note {
        return Note(
            id = id,
            title = title,
            content = content,
            isChecklist = isChecklist,
            checklistItems = checklistItems.map { it.toDomain() },
            color = NoteColor.valueOf(color.name),
            isPinned = isPinned,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }

    private fun com.dailyfocus.data.local.entity.ChecklistItem.toDomain(): ChecklistItem {
        return ChecklistItem(
            text = text,
            isChecked = isChecked
        )
    }

    private fun Note.toEntity(): NoteEntity {
        return NoteEntity(
            id = id,
            title = title,
            content = content,
            isChecklist = isChecklist,
            checklistItems = checklistItems.map { it.toEntity() },
            color = com.dailyfocus.data.local.entity.NoteColor.valueOf(color.name),
            isPinned = isPinned,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }

    private fun ChecklistItem.toEntity(): com.dailyfocus.data.local.entity.ChecklistItem {
        return com.dailyfocus.data.local.entity.ChecklistItem(
            text = text,
            isChecked = isChecked
        )
    }
}
