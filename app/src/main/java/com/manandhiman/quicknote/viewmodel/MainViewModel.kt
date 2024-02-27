package com.manandhiman.quicknote.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.manandhiman.quicknote.database.DatabaseHandler
import com.manandhiman.quicknote.model.Note

class MainViewModel(db: DatabaseHandler) : ViewModel() {

  private val db = db

  private val _notes: MutableState<List<Note>>
    get() = mutableStateOf(db.readNotes())

  val notes: State<List<Note>>
  get() = _notes

  fun updateNote(note: Note) {
    db.updateNote(note)
//    noteDao.updateNote(note.id, note.title, note.content, System.currentTimeMillis())
//    this._notes.value = noteDao.getAllNotes()
  }

  fun addNote(note: Note) {
    db.createNote(note)
  }

  fun deleteNote(note: Note) {
//    noteDao.deleteNote(note)
//    this._notes.value = noteDao.getAllNotes()
  }

}