package com.zedzak.zednotelite.state

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.zedzak.zednotelite.data.NotesRepository
import com.zedzak.zednotelite.model.Note
import java.util.UUID

object AppState {

    // Observable list of notes
    val notes = mutableStateListOf<Note>()

    // Currently selected note (for editor)
    var activeNoteId by mutableStateOf<String?>(null)
        private set

    init {
        // initial load
        notes.addAll(NotesRepository.getAllNotes())
    }

    fun createNewNote() {
        val note = Note(
            id = UUID.randomUUID().toString(),
            title = "",
            content = "",
            lastUpdated = System.currentTimeMillis()
        )
        NotesRepository.addNote(note)
        notes.add(note)
        activeNoteId = note.id
    }

    fun openNote(noteId: String) {
        activeNoteId = noteId
    }

    fun updateNote(updated: Note) {
        NotesRepository.updateNote(updated)

        val index = notes.indexOfFirst { it.id == updated.id }
        if (index != -1) {
            notes[index] = updated
        }
    }

    fun getActiveNote(): Note? {
        return activeNoteId?.let { id ->
            notes.find { it.id == id }
        }
    }
}

