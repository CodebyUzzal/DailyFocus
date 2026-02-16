package com.dailyfocus.domain.usecase.note

data class NoteUseCases(
    val getNotes: GetNotesUseCase,
    val getNoteById: GetNoteByIdUseCase,
    val addNote: AddNoteUseCase,
    val updateNote: UpdateNoteUseCase,
    val deleteNote: DeleteNoteUseCase,
    val togglePin: TogglePinUseCase,
    val toggleChecklistItem: ToggleChecklistItemUseCase
)
