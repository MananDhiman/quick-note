package com.manandhiman.quicknote.ui.screen

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.manandhiman.quicknote.model.Notebook
import com.manandhiman.quicknote.viewmodel.MainViewModel

@Composable
fun NotebooksScreen(navController: NavHostController, viewModel: MainViewModel) {
  NotebooksScreen(viewModel.notebooks, viewModel::addNotebook, viewModel::updateNotebook, viewModel::deleteNotebook, navController::popBackStack)
}

@Composable
fun NotebooksScreen(
  notebooks: State<List<Notebook>>,
  addNotebook: (String) -> Unit,
  updateNotebook: (Notebook) -> Unit,
  deleteNotebook: (Notebook) -> Unit,
  popBackStack: () -> Unit,
) {
  Scaffold(
    topBar = {
      Row(
        Modifier
          .background(Color.LightGray)
          .fillMaxWidth()
          .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Icon(
          imageVector = Icons.Default.ArrowBack,
          contentDescription = "Go Back",
          Modifier
            .clickable { popBackStack() }
            .size(32.dp)
        )
      }
    }
  ) { paddingValues ->

    Column(
      Modifier
        .fillMaxSize()
        .padding(paddingValues)
    ) {

      Row(
        Modifier
          .fillMaxWidth()
          .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        var name by remember { mutableStateOf("") }

        OutlinedTextField(
          value = name,
          onValueChange = { name = it },
          label = { Text(text = "Enter New Notebook Name") },
          modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Button(onClick = { if(name.isNotBlank()) addNotebook(name) }) {
          Text(text = "Add")
        }
      }

      LazyColumn(Modifier.padding(16.dp)) {
        items(notebooks.value.size) {

          val isNotebookEditDialogVisible = remember { mutableStateOf(false) }

          Row(
            Modifier
              .fillMaxWidth()
              .clickable {
                isNotebookEditDialogVisible.value = true
              }
          ) {
            Text(
              text = notebooks.value[it].name,
              fontSize = 24.sp
            )

            if (isNotebookEditDialogVisible.value)
              NotebookEditDialog(isNotebookEditDialogVisible, updateNotebook, deleteNotebook, notebooks.value[it])

          }

          Divider()
          Spacer(modifier = Modifier.height(16.dp))
        }
      }
    }
  }
}

@Composable
fun NotebookEditDialog(
  openDialog: MutableState<Boolean>,
  updateNotebook: (Notebook) -> Unit,
  deleteNotebook: (Notebook) -> Unit,
  notebook: Notebook
) {
  Dialog(onDismissRequest = { openDialog.value = false }) {

    val context = LocalContext.current

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

        var notebookName by remember { mutableStateOf(notebook.name) }

        Row(Modifier.fillMaxWidth()) {
          Text(text = "Edit or Delete Notebook", fontSize = 24.sp)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row {
          OutlinedTextField(
            value = notebookName,
            onValueChange = { notebookName = it },
            label = {
              Text(text = "Notebook Name")
            }
          )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
          Button(
            onClick = { openDialog.value = false },
            colors = ButtonDefaults.buttonColors(
              containerColor = Color.Gray,
              contentColor = Color.Black)
          ) {
            Text(text = "Cancel", maxLines = 1)
          }
          Button(
            onClick = {
              if (notebookName.isBlank()) {
                Toast.makeText(context, "Notebook cannot have blank name", Toast.LENGTH_SHORT).show()
              } else {
                updateNotebook(Notebook(notebook.id, notebookName))
                openDialog.value = false
              }
            },
          ) {
            Text(text = "Change Name", maxLines = 1)
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
          onClick = {
            deleteNotebook(notebook)
            openDialog.value = false
          },
          colors = ButtonDefaults.buttonColors(
            containerColor = Color.Red,
            contentColor = Color.Black)
        ) {
          Text(text = "Delete", maxLines = 1)
        }
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
private fun NotebooksScreenPrev() {
   NotebooksScreen(
     mutableStateOf(
       listOf(
         Notebook("1", "Log"),
         Notebook("2", "History"),
         Notebook("2", "ToDo"),
         Notebook("2", "Records"),
      )
     ),
     {},
     {},
     { 1 },
     {}
   )

//  NotebookEditDialog(
//    openDialog = mutableStateOf(true),
//    updateNotebook = { {} },
//    deleteNotebook = { {} },
//    notebook = Notebook("1", "Dates")
//  )
}