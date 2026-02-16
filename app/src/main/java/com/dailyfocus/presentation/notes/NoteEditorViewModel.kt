package com.dailyfocus.presentation.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dailyfocus.domain.model.ChecklistItem
import com.dailyfocus.domain.model.Note
import com.dailyfocus.domain.model.NoteColor
import com.dailyfocus.domain.usecase.note.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteEditorViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases
) : ViewModel() {

    private val _currentNote = MutableStateFlow<Note?>(null)
    val currentNote: StateFlow<Note?> = _currentNote.asStateFlow()

    private var originalNote: Note? = null

    fun loadNote(id: Long) {
        if (id == -1L) {
             // New Note
             _currentNote.value = Note(title = "", content = "", isChecklist = false) // Default
        } else {
            viewModelScope.launch {
                noteUseCases.getNoteById(id).collect {
                    _currentNote.value = it
                    if (originalNote == null) originalNote = it // Track original for dirty check if needed
                }
            }
        }
    }
    
    fun initNewNote(isChecklist: Boolean) {
        _currentNote.value = Note(title = "", content = "", isChecklist = isChecklist)
    }

    fun updateTitle(title: String) {
        _currentNote.value = _currentNote.value?.copy(title = title)
    }

    fun updateContent(content: String) {
        _currentNote.value = _currentNote.value?.copy(content = content)
    }
    
    fun updateColor(color: NoteColor) {
        _currentNote.value = _currentNote.value?.copy(color = color)
    }

    fun togglePin() {
        _currentNote.value = _currentNote.value?.copy(isPinned = !(_currentNote.value?.isPinned ?: false))
    }
    
    fun addChecklistItem(text: String) {
        val current = _currentNote.value ?: return
        val newItems = current.checklistItems + ChecklistItem(text = text, isChecked = false)
        _currentNote.value = current.copy(checklistItems = newItems)
    }
    
    fun updateChecklistItem(index: Int, text: String, isChecked: Boolean) {
         val current = _currentNote.value ?: return
         if (index < 0 || index >= current.checklistItems.size) return
         val newItems = current.checklistItems.toMutableList()
         newItems[index] = ChecklistItem(text = text, isChecked = isChecked)
         _currentNote.value = current.copy(checklistItems = newItems)
    }
    
    fun removeChecklistItem(index: Int) {
         val current = _currentNote.value ?: return
         if (index < 0 || index >= current.checklistItems.size) return
         val newItems = current.checklistItems.toMutableList()
         newItems.removeAt(index)
         _currentNote.value = current.copy(checklistItems = newItems)
    }

    fun saveNote() {
        val note = _currentNote.value ?: return
        if (note.title.isNullOrBlank() && note.content.isNullOrBlank() && note.checklistItems.isEmpty()) return
        
        viewModelScope.launch {
            if (note.id == 0L) {
                noteUseCases.addNote(note)
            } else {
                noteUseCases.updateNote(note)
            }
        }
    }
    
    fun deleteNote() {
        val note = _currentNote.value ?: return
        if (note.id == 0L) return // Not saved yet
        viewModelScope.launch {
            noteUseCases.deleteNote(note)
        }
    }
}
