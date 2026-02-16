package com.dailyfocus.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class NoteWithContent(
    @Embedded val note: NoteEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "noteId"
    )
    val contents: List<NoteContentEntity>
)
