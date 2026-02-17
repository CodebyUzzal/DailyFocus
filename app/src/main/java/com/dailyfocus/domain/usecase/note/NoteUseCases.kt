package com.dailyfocus.domain.usecase.note

data class NoteUseCases(
    val getNotes: GetNotesUseCase,
    val getNoteDetail: GetNoteDetailUseCase,
    val saveNote: SaveNoteUseCase,
    val deleteNote: DeleteNoteUseCase,
    val togglePin: TogglePinUseCase,
    val toggleChecklistItem: ToggleChecklistItemUseCase,
    val reorderChecklist: ReorderChecklistUseCase,
    val deleteAllNotes: DeleteAllNotesUseCase
)
