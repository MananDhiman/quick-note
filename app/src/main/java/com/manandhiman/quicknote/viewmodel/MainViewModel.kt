package com.manandhiman.quicknote.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manandhiman.quicknote.database.DatabaseHandler
import com.manandhiman.quicknote.model.Note
import com.manandhiman.quicknote.model.Notebook
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val db: DatabaseHandler) : ViewModel() {

  val notebookSelected = mutableStateOf("")

  val notes = mutableStateOf(db.readNotes(notebookSelected.value))

  val notebooks = mutableStateOf(db.readNotebooks())

  fun addNote(note: Note) {
    viewModelScope.launch(Dispatchers.IO) {
      db.createNote(note)
      notes.value = db.readNotes(notebookSelected.value)
    }
  }

  fun updateNote(note: Note) {
    viewModelScope.launch(Dispatchers.IO) {
      db.updateNote(note)
      notes.value = db.readNotes(notebookSelected.value)
    }
  }

  fun deleteNote(note: Note) {
    viewModelScope.launch(Dispatchers.IO) {

      db.deleteNote(note.id!!)
      notes.value = db.readNotes(notebookSelected.value)
    }
  }

  fun addNotebook(notebook: String) {
    viewModelScope.launch(Dispatchers.IO) {
      val index = db.createNotebook(notebook)
      notebooks.value = db.readNotebooks()
    }
  }

  fun updateNotebook(notebook: Notebook) {
    viewModelScope.launch(Dispatchers.IO) {
      db.updateNotebook(notebook)
      notebooks.value = db.readNotebooks()
    }
  }

  fun deleteNotebook(notebook: Notebook) {
    viewModelScope.launch(Dispatchers.IO) {
      val rowsDeleted = db.deleteNotebook(notebook.id)
      if(rowsDeleted == 1) db.updateParentNotebook(notebook.id, "")
    }
  }

  fun selectedNotebook(notebook: String) {
    viewModelScope.launch(Dispatchers.IO) {
      notebookSelected.value = notebook
      notes.value = db.readNotes(notebookSelected.value)
    }
  }

  fun assignNotebook(note: Note, notebookIndex: String) {
    viewModelScope.launch(Dispatchers.IO) {
      db.setParentNotebookOfNote(notebookIndex, note.id!!)
    }
  }

}