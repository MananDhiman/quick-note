package com.manandhiman.quicknote.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.manandhiman.quicknote.model.Note
import com.manandhiman.quicknote.viewmodel.MainViewModel

@Composable
fun MainScreen(
  navController: NavHostController,
  viewModel: MainViewModel,
  ) {
  Scaffold(
    // create new note button
    floatingActionButton = {
      Button(
        onClick = { navController.navigate("note/-1") }, // -1 is index for new note
        Modifier.size(75.dp),
        elevation = ButtonDefaults.buttonElevation(
          defaultElevation = 8.dp,
          pressedElevation = 2.dp
        )
      ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "Create New Note")
      }
    }
  ) {
    val x = it


    if(viewModel.notes.value.isEmpty()) {
      Column(
        Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
      ) {
        Text(text = "No Notes Added. Use the add new (+) button to create a new note.",
          fontSize = 28.sp, lineHeight = 36.sp)
      }

    }
    else {
      LazyColumn(
        Modifier
          .fillMaxSize()
          .padding(16.dp)
      ) {

        items(viewModel.notes.value.size) { index ->
          PostUI(viewModel.notes.value[index], index, navController, viewModel::deleteNote)
        }
      }
    }


  }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PostUI(note: Note, index: Int, navController: NavHostController, deleteNote: (Note) -> Unit) {

  val deleteNoteConfirmDialog = remember{ mutableStateOf(false) }

  Column(
    Modifier
      .combinedClickable(
        onClick = { navController.navigate("note/$index") }, // clicking item takes to note detail
        onLongClick = { deleteNoteConfirmDialog.value = true } // long click for delete
      )
  ) {
    Text(text = note.title, fontSize = 24.sp, maxLines = 3, overflow = TextOverflow.Ellipsis)
    Divider()
    Spacer(modifier = Modifier.height(8.dp))
  }

  if(deleteNoteConfirmDialog.value) deleteNotDialog(deleteNoteConfirmDialog, note, deleteNote)

}

@Composable
private fun deleteNotDialog(
  deleteNoteConfirmDialog: MutableState<Boolean>,
  note: Note,
  deleteNote: (Note) -> Unit
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
        onClick = { deleteNote(note) },
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

@Preview(showBackground = true)
@Composable
fun Prev() {
  MainScreen(navController = rememberNavController(), viewModel = viewModel())
}