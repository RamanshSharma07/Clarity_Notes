package com.ramanshsharma07.claritynotes.di

import androidx.room.Room
import com.ramanshsharma07.claritynotes.feature_note.data.data_source.NoteDao
import com.ramanshsharma07.claritynotes.feature_note.data.data_source.NoteDatabase
import com.ramanshsharma07.claritynotes.feature_note.data.repository.NoteRepositoryImpl
import com.ramanshsharma07.claritynotes.feature_note.domain.repository.NoteRepository
import com.ramanshsharma07.claritynotes.feature_note.domain.use_cases.AddNoteUseCase
import com.ramanshsharma07.claritynotes.feature_note.domain.use_cases.DeleteNoteUseCase
import com.ramanshsharma07.claritynotes.feature_note.domain.use_cases.GetNoteUseCase
import com.ramanshsharma07.claritynotes.feature_note.domain.use_cases.GetNotesUseCase
import com.ramanshsharma07.claritynotes.feature_note.domain.use_cases.NoteUseCases
import com.ramanshsharma07.claritynotes.feature_note.presentation.add_edit_note.AddEditNoteViewModel
import com.ramanshsharma07.claritynotes.feature_note.presentation.notes.NotesViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<NoteDatabase> {
        Room.databaseBuilder(
            androidApplication(), // Provides the Application context
            NoteDatabase::class.java,
            NoteDatabase.DATABASE_NAME
        ).build()
    }

    single<NoteRepository> {
        NoteRepositoryImpl(dao = get())
    }

    single<NoteDao> {
        val database = get<NoteDatabase>()
        database.noteDao
    }

    single {
        NoteUseCases(
            getNotes = GetNotesUseCase(get()),
            deleteNote = DeleteNoteUseCase(get()),
            addNote = AddNoteUseCase(get()),
            getNote = GetNoteUseCase(get())
        )
    }


    viewModel {
        NotesViewModel(
            noteUseCases = get()
        )
    }

    viewModel { (savedStateHandle: androidx.lifecycle.SavedStateHandle) ->
        AddEditNoteViewModel(
            noteUseCases = get(),
            savedStateHandle = savedStateHandle
        )
    }
}