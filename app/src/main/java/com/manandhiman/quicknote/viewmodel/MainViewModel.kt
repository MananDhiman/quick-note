package com.manandhiman.quicknote.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.manandhiman.quicknote.database.DatabaseHandler
import com.manandhiman.quicknote.model.Note
import com.manandhiman.quicknote.model.Notebook

class MainViewModel(private val db: DatabaseHandler) : ViewModel() {

  val notebookSelected = mutableStateOf("")

  val notes = mutableStateOf(db.readNotes(notebookSelected.value))

  val notebooks = mutableStateOf(db.readNotebooks())

  fun addNote(note: Note) {
    db.createNote(note)
    notes.value = db.readNotes(notebookSelected.value)
  }

  fun updateNote(note: Note) {
    db.updateNote(note)
    notes.value = db.readNotes(notebookSelected.value)
  }

  fun deleteNote(note: Note) {
    db.deleteNote(note.id!!)
    notes.value = db.readNotes(notebookSelected.value)
  }

  fun addNotebook(notebook: String) {
    val index = db.createNotebook(notebook)
    notebooks.value = db.readNotebooks()
  }

  fun updateNotebook(notebook: Notebook) {
    db.updateNotebook(notebook)
    notebooks.value = db.readNotebooks()
  }

  fun deleteNotebook(notebook: Notebook): Int {
    val rowsDeleted = db.deleteNotebook(notebook.id)
    if(rowsDeleted == 1) db.updateParentNotebook(notebook.id, "")

    return rowsDeleted
//    notebooks.value = db.readNotebooks()

  }

  fun selectedNotebook(notebook: String) {
    notebookSelected.value = notebook
    notes.value = db.readNotes(notebookSelected.value)
  }

  fun assignNotebook(note: Note, notebookIndex: String) {
    db.setParentNotebookOfNote(notebookIndex, note.id!!)
  }

}