package com.dailyfocus.presentation.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dailyfocus.domain.model.Note
import com.dailyfocus.domain.usecase.note.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases
) : ViewModel() {

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes.asStateFlow()
    
    // We could expose a UI state object if we had loading/error states, 
    // but likely the Flow from Room will just emit defaults. 
    // Let's stick to simple flow for now, or wrap it if needed. 
    // Validating requirements: "No empty blank grid. If empty: Show centered illustration".
    // We can derive "isEmpty" from the list in the UI.

    init {
        getNotes()
    }

    private fun getNotes() {
        viewModelScope.launch {
            noteUseCases.getNotes()
                .collect {
                    _notes.value = it
                }
        }
    }

    fun addNote(title: String, content: String, isChecklist: Boolean) {
        viewModelScope.launch {
            val items = if (content.isNotBlank()) {
                listOf(com.dailyfocus.domain.model.NoteContent(text = content, position = 0))
            } else {
                emptyList()
            }
            
            noteUseCases.saveNote(
                Note(
                    title = title,
                    isChecklist = isChecklist,
                    items = items
                )
            )
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            noteUseCases.deleteNote(note)
        }
    }

    fun togglePin(note: Note) {
        viewModelScope.launch {
            noteUseCases.togglePin(note)
        }
    }
    
    // Editor related logic might be in a separate ViewModel or shared. 
    // Given "NoteEditorScreen", it might need its own VM to handle "GetNoteById" and auto-save.
    // Let's create a separate NoteEditorViewModel for the editor screen to keep things clean.
    // However, fast interactions on the main screen (like toggle checklist item on card) need to be handled here.
    
    fun toggleChecklistItem(note: Note, itemIndex: Int) {
        viewModelScope.launch {
            noteUseCases.toggleChecklistItem(note, itemIndex)
        }
    }
}
