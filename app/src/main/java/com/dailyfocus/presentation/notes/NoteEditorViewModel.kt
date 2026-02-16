package com.dailyfocus.presentation.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dailyfocus.domain.model.Note
import com.dailyfocus.domain.model.NoteBackgroundStyle
import com.dailyfocus.domain.model.NoteContent
import com.dailyfocus.domain.usecase.note.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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

    private var autoSaveJob: Job? = null

    fun loadNote(id: Long) {
        if (id == -1L) {
             // New Note handled by initNewNote
        } else {
            viewModelScope.launch {
                noteUseCases.getNoteDetail(id).collect {
                    _currentNote.value = it
                }
            }
        }
    }
    
    fun initNewNote(isChecklist: Boolean) {
        if (_currentNote.value == null) {
            _currentNote.value = Note(
                title = "", 
                isChecklist = isChecklist,
                items = if (!isChecklist) listOf(NoteContent(text = "", position = 0)) else emptyList()
            )
        }
    }

    fun updateTitle(title: String) {
        _currentNote.value = _currentNote.value?.copy(title = title)
        triggerAutoSave()
    }

    // For Text Notes
    fun updateContent(content: String) {
        val current = _currentNote.value ?: return
        // Preserve ID if extending existing item
        val existingItem = current.items.firstOrNull()
        val newItem = existingItem?.copy(text = content) ?: NoteContent(text = content, position = 0)
        
        _currentNote.value = current.copy(items = listOf(newItem))
        triggerAutoSave()
    }
    
    fun addChecklistItem(text: String) {
        val current = _currentNote.value ?: return
        val newPosition = current.items.maxOfOrNull { it.position }?.plus(1) ?: 0
        val newItems = current.items + NoteContent(text = text, position = newPosition)
        _currentNote.value = current.copy(items = newItems)
        triggerAutoSave()
    }
    
    fun updateChecklistItem(index: Int, text: String, isChecked: Boolean) {
         val current = _currentNote.value ?: return
         if (index < 0 || index >= current.items.size) return
         val newItems = current.items.toMutableList()
         // Ensure we keep the ID and Position
         newItems[index] = newItems[index].copy(text = text, isChecked = isChecked)
         _currentNote.value = current.copy(items = newItems)
         triggerAutoSave()
    }
    
    fun removeChecklistItem(index: Int) {
         val current = _currentNote.value ?: return
         if (index < 0 || index >= current.items.size) return
         val newItems = current.items.toMutableList()
         newItems.removeAt(index)
         _currentNote.value = current.copy(items = newItems)
         triggerAutoSave()
    }
    
    fun onReorder(from: Int, to: Int) {
        val current = _currentNote.value ?: return
        val mutableItems = current.items.toMutableList()
        // Swap
        if (from < 0 || to < 0 || from >= mutableItems.size || to >= mutableItems.size) return
        
        val item = mutableItems.removeAt(from)
        mutableItems.add(to, item)
        
        // Re-index positions
        val reindexed = mutableItems.mapIndexed { index, it -> it.copy(position = index) }
        
        _currentNote.value = current.copy(items = reindexed)
        triggerAutoSave()
    }

    fun updateBackgroundStyle(style: NoteBackgroundStyle) {
        _currentNote.value = _currentNote.value?.copy(backgroundStyle = style)
        triggerAutoSave()
    }

    fun togglePin() {
        val current = _currentNote.value ?: return
        _currentNote.value = current.copy(isPinned = !current.isPinned)
        triggerAutoSave()
    }

    private fun triggerAutoSave() {
        autoSaveJob?.cancel()
        autoSaveJob = viewModelScope.launch {
            delay(300) // Debounce
            saveNoteImmediate()
        }
    }

    fun saveNote() {
        // Immediate save (e.g. on back press)
        autoSaveJob?.cancel()
        viewModelScope.launch {
            saveNoteImmediate()
        }
    }
    
    private suspend fun saveNoteImmediate() {
        val note = _currentNote.value ?: return
        if (note.title.isBlank() && note.items.all { it.text.isBlank() }) {
             // If ID=0 (new), and empty, don't save.
             if (note.id == 0L) return
        }
        val savedId = noteUseCases.saveNote(note)
        if (note.id == 0L) {
            _currentNote.value = note.copy(id = savedId)
        }
    }
    
    fun deleteNote() {
        val note = _currentNote.value ?: return
        if (note.id == 0L) return
        viewModelScope.launch {
            noteUseCases.deleteNote(note)
        }
    }
}
