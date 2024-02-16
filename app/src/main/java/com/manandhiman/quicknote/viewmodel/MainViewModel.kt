package com.manandhiman.quicknote.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.manandhiman.quicknote.database.NoteDao
import com.manandhiman.quicknote.model.Note

class MainViewModel(): ViewModel() {

  private lateinit var noteDao: NoteDao

  private val _notes = mutableStateOf(listOf<Note>())
  val notes: State<List<Note>> get() = _notes

  fun setNoteDao(noteDao: NoteDao) {
    this.noteDao = noteDao
    this._notes.value = noteDao.getAllNotes()
  }

  fun updateNote(note: Note) {
    noteDao.updateNote(note.id, note.title, note.note)
  }

  fun addNote(note: Note) {
    noteDao.createNewNote(note)
  }

}