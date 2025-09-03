package com.ramanshsharma07.claritynotes.feature_note.data.repository

import com.ramanshsharma07.claritynotes.feature_note.data.data_source.NoteDao
import com.ramanshsharma07.claritynotes.feature_note.domain.model.Note
import com.ramanshsharma07.claritynotes.feature_note.domain.repository.NoteRepository

class NoteRepositoryImpl(
    private val dao: NoteDao
): NoteRepository {

    override fun getNotes() = dao.getNotes()

    override suspend fun getNoteById(id: Int) = dao.getNoteById(id)

    override suspend fun insertNote(note: Note) = dao.insertNote(note)

    override suspend fun deleteNote(note: Note) = dao.deleteNote(note)

}