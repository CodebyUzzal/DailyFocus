package com.dailyfocus.di

import com.dailyfocus.domain.repository.NoteRepository
import com.dailyfocus.domain.usecase.note.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Provides
    @Singleton
    fun provideNoteUseCases(repository: NoteRepository): NoteUseCases {
        return NoteUseCases(
            getNotes = GetNotesUseCase(repository),
            getNoteDetail = GetNoteDetailUseCase(repository),
            saveNote = SaveNoteUseCase(repository),
            deleteNote = DeleteNoteUseCase(repository),
            togglePin = TogglePinUseCase(repository),
            toggleChecklistItem = ToggleChecklistItemUseCase(repository),
            reorderChecklist = ReorderChecklistUseCase(repository),
            deleteAllNotes = DeleteAllNotesUseCase(repository)
        )
    }
}
