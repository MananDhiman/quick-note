package com.manandhiman.quicknote.ui.screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.manandhiman.quicknote.model.Note
import com.manandhiman.quicknote.model.Notebook
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
    Note(title = "New Note Title Here...", content = "Note Content Here...")
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

  val showDropDown = remember { mutableStateOf(false) }

  Row(
    Modifier
      .background(Color.LightGray)
      .fillMaxWidth()
      .padding(16.dp),
    horizontalArrangement = Arrangement.SpaceBetween)
  {

    val context = LocalContext.current // used to display toast
    val openInfoDialog = remember { mutableStateOf(false) }
    val deleteNoteConfirmDialog = remember{ mutableStateOf(false) }
    val assignNotebookDialog = remember { mutableStateOf(false) }

    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Go Back", Modifier.clickable { navController.popBackStack() }.size(32.dp))
    Row {

      if(index != -1) {
        Box {

          Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = "More Options",
            modifier = Modifier.clickable { showDropDown.value = true }.size(32.dp)
          )
          DropdownMenu(
            expanded = showDropDown.value,
            onDismissRequest = { showDropDown.value = false }
          ) {
            DropdownMenuItem(
              text = { Text("Assign to Notebook") },
              onClick = { assignNotebookDialog.value = true }
            )

            Divider()

            DropdownMenuItem(
              text = { Text("Note Information") },
              onClick = { openInfoDialog.value = true }
            )

            Divider()

            DropdownMenuItem(
              text = {  Text("Delete Note") },
              onClick = {
                deleteNoteConfirmDialog.value = true
              }
            )
          }
        }
      }

      Spacer(modifier = Modifier.width(4.dp))
      Icon(imageVector = Icons.Default.Done, contentDescription = "Save Note", Modifier.clickable {
        doneNoteAction(index, viewModel, noteTitle, noteBody, context, navController, note)
      }.size(32.dp))
    }

    if(openInfoDialog.value) NoteInfoDialog(openInfoDialog, noteBody, note.createdAt, note.updatedAt)
    if(deleteNoteConfirmDialog.value) DeleteNoteDialog(deleteNoteConfirmDialog, note, viewModel::deleteNote, navController)
    if(assignNotebookDialog.value) AssignNotebookDialog(assignNotebookDialog, viewModel.notebooks.value.toList(), note, viewModel::assignNotebook)

  }
}

@Composable
fun AssignNotebookDialog(openDialog: MutableState<Boolean>, notebooks: List<Notebook>, note: Note, assignNotebook: (Note, String) -> Unit) {
  Dialog(onDismissRequest = { openDialog.value = false }) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .height(330.dp)
        .padding(16.dp),
      shape = RoundedCornerShape(16.dp),
    ) {

      Column(
        Modifier
          .fillMaxSize()
          .padding(16.dp)
      ) {
        Row(Modifier.fillMaxWidth()) {

          Text(text = "Choose Notebook to assign note to", fontSize = 24.sp)
        }
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn() {

          item {
            Row(
              Modifier
                .fillMaxWidth()
                .clickable {
                  assignNotebook(note, "")
                  openDialog.value = false
                }
                .padding(16.dp)
            ) {
              Text(text = "None (Remove from any notebook)", fontSize = 16.sp)
            }
            Divider()
          }

          items(notebooks.size) {

            if (note.parentNotebook != notebooks[it].id) {
              Row(
                Modifier
                  .fillMaxWidth()
                  .clickable {
                    assignNotebook(note, notebooks[it].id)
                    openDialog.value = false
                  }
                  .padding(16.dp)
              ) {
                Text(text = notebooks[it].name, fontSize = 16.sp)
              }
              Divider()
            }

          }
        }
      }

    }
  }
}

@Preview
@Composable
private fun Prev() {
  AssignNotebookDialog(
    mutableStateOf(true), listOf(
      Notebook("1","Notes"),
      Notebook("1","Notes"),
      Notebook("1","Notes"),
    ),
    Note(null, "Title","Content",),
    { _, _ -> {}  }
  )
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
    viewModel.updateNote(Note(id = note.id, title = noteTitle, content =  noteBody))
    Toast.makeText(context, "Note Updated", Toast.LENGTH_SHORT).show()
    navController.popBackStack()
  }
}

@Composable
fun NoteInfoDialog(
  openDialog: MutableState<Boolean>,
  noteBody: String,
  createdAt: String,
  updatedAt: String
) {

  val formattedCreatedAtTime = SimpleDateFormat("dd/MM/yyyy h:mm a")
    .format(Date(createdAt.toLong()))
  val formattedLastUpdatedAtTime = SimpleDateFormat("dd/MM/yyyy h:mm a")
    .format(Date(updatedAt.toLong()))

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
  )
}

@Composable
private fun DeleteNoteDialog(
  deleteNoteConfirmDialog: MutableState<Boolean>,
  note: Note,
  deleteNote: (Note) -> Unit,
  navController: NavHostController
) {
  AlertDialog(
    onDismissRequest = { deleteNoteConfirmDialog.value = false },
    title = { Text(text = "Delete Note?") },
    text = {
      Text(
        "Are you sure you want to delete note:\n${note.title}",
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
      )
    },
    confirmButton = {
      Button(
        onClick = {
          navController.navigate("main")
          deleteNote(note)
        },
        colors = ButtonDefaults.buttonColors(
          containerColor = Color.Red
        )
      ) { Text("Yes, Delete") }
    },
    dismissButton = {
      Button(
        onClick = { deleteNoteConfirmDialog.value = false }
      ) { Text("No, don't delete") }
    }
  )
}