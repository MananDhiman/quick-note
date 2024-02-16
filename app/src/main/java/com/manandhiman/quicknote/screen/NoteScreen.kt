package com.manandhiman.quicknote.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.manandhiman.quicknote.model.Note
import com.manandhiman.quicknote.viewmodel.MainViewModel

@Preview(showBackground = true)
@Composable
fun NoteScreenPrev() {
  NoteScreen(rememberNavController(), viewModel(), -1)
}

@Composable
fun NoteScreen(navController: NavHostController, viewModel: MainViewModel, index: Int) {

  // if -1, new note, else existing note
  val note = if(index == -1) {
    Note()
  } else {
    viewModel.notes.value[index]
  }

  var noteTitle by remember { mutableStateOf(note.title) }
  var noteBody by remember { mutableStateOf(note.note) }


  Scaffold(
    topBar = { TopAppBar(index, viewModel, noteTitle, noteBody, navController, note) } // top bar for back and save button
  ) {
    Column(
      Modifier
        .fillMaxWidth()
        .padding(top = it.calculateTopPadding())) {
      TextField(value = noteTitle, onValueChange = { title -> noteTitle = title})
      TextField(value = noteBody, onValueChange = {body -> noteBody = body})
    }
  }

}

@Composable
fun TopAppBar(
  index: Int,
  viewModel: MainViewModel,
  noteTitle: String,
  noteBody: String,
  navController: NavHostController,
  note: Note
) {
  Row(
    Modifier
      .background(Color.LightGray)
      .fillMaxWidth()
      .padding(16.dp),
    horizontalArrangement = Arrangement.SpaceBetween)
  {

    val context = LocalContext.current // used to display toast

    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Go Back", Modifier.clickable { navController.popBackStack() })
    Icon(imageVector = Icons.Default.Done, contentDescription = "Save Note", Modifier.clickable {
      if(index == -1) {
        viewModel.addNote(Note(title = noteTitle, note = noteBody))
        Toast.makeText(context, "Note Created", Toast.LENGTH_SHORT).show()
      } else {
        viewModel.updateNote(Note(note.id, noteTitle, noteBody))
        Toast.makeText(context, "Note Updated", Toast.LENGTH_SHORT).show()
      }
    })
  }
}