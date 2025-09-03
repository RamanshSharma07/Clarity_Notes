package com.ramanshsharma07.claritynotes.feature_note.presentation.add_edit_note

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramanshsharma07.claritynotes.feature_note.domain.model.Note
import com.ramanshsharma07.claritynotes.feature_note.domain.use_case.NoteUseCases
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class AddEditNoteViewModel (
    private val noteUseCases: NoteUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var noteTitleState = mutableStateOf(NoteTextFieldState(
        hint = "Enter title..."
    ))
        private set

    var noteContentState = mutableStateOf(NoteTextFieldState(
        hint = "Enter some content"
    ))
        private set

    var noteColorState = mutableStateOf(Note.noteColors.random().toArgb())
        private set

    var eventFlow = MutableSharedFlow<UiEvent>()
        private set

    private var currentNoteId: Int? = null

    init {
        savedStateHandle.get<Int>("noteId")?.let { noteId ->
            if(noteId != -1) {
                viewModelScope.launch {
                    noteUseCases.getNote(noteId)?.also { note ->
                        currentNoteId = note.id
                        noteTitleState.value = noteTitleState.value.copy(
                            text = note.title,
                            isHintVisible = false
                        )
                        noteContentState.value = noteContentState.value.copy(
                            text = note.content,
                            isHintVisible = false
                        )
                        noteColorState.value = note.color
                    }
                }
            }

        }
    }

    fun onEvent(event: AddEditNoteEvent) {
        when(event) {

            is AddEditNoteEvent.EnteredTitle -> {
                noteTitleState.value = noteTitleState.value.copy(
                    text = event.value
                )
            }

            is AddEditNoteEvent.ChangeTitleFocus -> {
                noteTitleState.value = noteTitleState.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            noteTitleState.value.text.isBlank()
                )
            }

            is AddEditNoteEvent.EnteredContent -> {
                noteContentState.value = noteContentState.value.copy(
                    text = event.value
                )
            }

            is AddEditNoteEvent.ChangeContentFocus -> {
                noteContentState.value = noteContentState.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            noteContentState.value.text.isBlank()
                )
            }

            is AddEditNoteEvent.ChangeColor -> {
                noteColorState.value = event.color
            }

            is AddEditNoteEvent.SaveNote -> {
                viewModelScope.launch {
                    try {
                        noteUseCases.addNote(
                            Note(
                                title = noteTitleState.value.text,
                                content = noteContentState.value.text,
                                timestamp = System.currentTimeMillis(),
                                color = noteColorState.value,
                                id = currentNoteId
                            )
                        )
                        eventFlow.emit(UiEvent.SaveNote)
                    } catch(e: Exception) {
                        eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message ?: "Couldn't save note"
                            )
                        )
                    }

                }
            }

        }
    }

    sealed class UiEvent{
        data class ShowSnackbar(val message: String): UiEvent()
        object SaveNote: UiEvent()
    }
}