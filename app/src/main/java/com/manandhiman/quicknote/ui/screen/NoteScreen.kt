package com.manandhiman.quicknote.ui.screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import java.text.SimpleDateFormat
import java.util.Date

@Preview(showBackground = true)
@Composable
fun NoteScreenPrev() {
  NoteScreen(rememberNavController(), viewModel(), -1)
}

@Composable
fun NoteScreen(navController: NavHostController, viewModel: MainViewModel, index: Int) {

  // if -1, new note, else existing note
  val note = if(index == -1) {
    Note("","","")
  } else {
    viewModel.notes.value[index]
  }

  var noteTitle by remember { mutableStateOf(note.title) }
  var noteBody by remember { mutableStateOf(note.content) }

  Scaffold(
    topBar = { TopAppBar(index, viewModel, noteTitle, noteBody, navController, note) } // top bar for back and save button
  ) {
    Column(
      Modifier
        .fillMaxWidth()
        .padding(top = it.calculateTopPadding() + 16.dp, start = 16.dp, end = 16.dp, bottom = 32.dp)
//        .verticalScroll(rememberScrollState())
      ,
      verticalArrangement = Arrangement.Center,

      ) {

      TextField(value = noteTitle, onValueChange = { title -> noteTitle = title}, modifier = Modifier.fillMaxWidth())

      Spacer(modifier = Modifier.height(32.dp))

      TextField(
        value = noteBody,
        onValueChange = {body -> noteBody = body},
        modifier = Modifier.fillMaxSize(),

      )
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
    val openInfoDialog = remember { mutableStateOf(false) }

    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Go Back", Modifier.clickable { navController.popBackStack() })
    Row {
      Icon(imageVector = Icons.Default.Info, contentDescription = "Note Information", Modifier.clickable { openInfoDialog.value = true })
      Spacer(modifier = Modifier.width(4.dp))
      Icon(imageVector = Icons.Default.Done, contentDescription = "Save Note", Modifier.clickable {
        doneNoteAction(index, viewModel, noteTitle, noteBody, context, navController, note)
      })

    }
    
//    if(openInfoDialog.value) NoteInfoDialog(openInfoDialog, noteBody, note.createdAt, note.updatedAt)

  }
}

private fun doneNoteAction(
  index: Int,
  viewModel: MainViewModel,
  noteTitle: String,
  noteBody: String,
  context: Context,
  navController: NavHostController,
  note: Note
) {
  if (index == -1) {
    viewModel.addNote(Note(title = noteTitle, content = noteBody))
    Toast.makeText(context, "Note Created", Toast.LENGTH_SHORT).show()
    navController.popBackStack()
  } else {
//    viewModel.updateNote(Note(note.id, noteTitle, noteBody))
    Toast.makeText(context, "Note Updated", Toast.LENGTH_SHORT).show()
    navController.popBackStack()
  }
}

@Composable
fun NoteInfoDialog(
  openDialog: MutableState<Boolean>,
  noteBody: String,
  createdAt: Long,
  updatedAt: Long
) {

  val formattedCreatedAtTime = SimpleDateFormat("dd/MM/yyyy h:mm a")
    .format(Date(createdAt))
  val formattedLastUpdatedAtTime = SimpleDateFormat("dd/MM/yyyy h:mm a")
    .format(Date(updatedAt))

  AlertDialog(
    onDismissRequest = { openDialog.value = false },
    title = { Text(text = "Note Details") },
    text = {
      Text("Characters: ${noteBody.length}" +
          "\nWords: ${noteBody.split(" ").size}" +
          "\nCreated At: $formattedCreatedAtTime" +
          "\nLast Modified: $formattedLastUpdatedAtTime"
      )
    },
    confirmButton = {
      Button(
        onClick = { openDialog.value = false }
      ) { Text("Ok") }
    },
//    dismissButton = {
//      Button(
//        onClick = { openDialog.value = false }
//      ) { Text("Dismiss Button") }
//    }
  )
}