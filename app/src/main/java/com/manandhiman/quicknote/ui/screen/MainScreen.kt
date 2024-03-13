package com.manandhiman.quicknote.ui.screen

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
    floatingActionButton = { FabCreateNote(navController) },
    topBar = { TopAppBar(navController) }
  ) {paddingValues ->

    Column(
      Modifier
        .fillMaxSize()
        .padding(paddingValues)
    ) {
      // Notebooks list on main screen
      val notebooks = viewModel.notebooks

      if (notebooks.value.isNotEmpty()) {
        Box(Modifier.padding(8.dp)) {
          Text(text = "Select Notebook", fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
        }
      }

      LazyRow(
        Modifier
          .fillMaxWidth()
          .padding(8.dp)
      ) {

          item {

              Button(
                onClick = { viewModel.selectedNotebook("") },
                shape = RoundedCornerShape(4.dp),
                enabled = viewModel.notebookSelected.value != ""
              ) {
                Text(text = "All")
              }

            Spacer(modifier = Modifier.width(4.dp))
          }

          items(notebooks.value.size) {
            Button(
              onClick = { viewModel.selectedNotebook(notebooks.value[it].name) },
              enabled = viewModel.notebookSelected.value != notebooks.value[it].name,
              shape = RoundedCornerShape(4.dp),
            ) {
              Text(text = notebooks.value[it].name)
            }
            Spacer(modifier = Modifier.width(4.dp))
          }

      }

      if(viewModel.notes.value.isEmpty()) {
        Column(
          Modifier
            .fillMaxSize()
            .padding(32.dp),
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
            .padding(
              top = 8.dp,
              start = 12.dp,
              end = 12.dp,
//              bottom = 32.dp
            )
        ) {
          
          item {
            Text(text = "Notes", fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(4.dp))
          }
          
          items(viewModel.notes.value.size) { index ->
            PostUI(viewModel.notes.value[index], index, navController)
          }
        }
      }
    }

  }
}

@Composable
private fun FabCreateNote(navController: NavHostController) {
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

@Composable
private fun PostUI(note: Note, index: Int, navController: NavHostController) {

  Column(
    Modifier
      .clickable { navController.navigate("note/$index") }
  ) {
    Row(
      Modifier
        .fillMaxWidth()
//        .background(color = Color(0xEEE,0xEEE,0xEEE, ))
        ,
      verticalAlignment = Alignment.CenterVertically,

    ) {
      Text(text = note.title, fontSize = 24.sp, maxLines = 3, overflow = TextOverflow.Ellipsis, modifier = Modifier.fillMaxWidth(0.9f))
      Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = "Open Note")
    }

    Divider()
    Spacer(modifier = Modifier.height(8.dp))
  }

}

@Composable
private fun TopAppBar(navController: NavHostController) {
  val showDropDown = remember { mutableStateOf(false) }
  Row(
    Modifier
      .background(Color.LightGray)
      .fillMaxWidth()
      .padding(16.dp),
    horizontalArrangement = Arrangement.SpaceBetween
  ) {

    Text(text = "Quick Note", fontWeight = FontWeight.Bold, fontSize = 24.sp)

    Box {
      Icon(
        imageVector = Icons.Default.MoreVert,
        contentDescription = "More Options",
        modifier = Modifier
          .clickable { showDropDown.value = true }
          .size(32.dp),

      )
      DropdownMenu(
        expanded = showDropDown.value,
        onDismissRequest = { showDropDown.value = false }
      ) {
        DropdownMenuItem(
          text = {  Text("Manage Notebooks") },
          onClick = { navController.navigate("notebooks") }
        )

        Divider()
        DropdownMenuItem(
          text = { Text("About") },
          onClick = { navController.navigate("about") }
        )
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
private fun Prev() {
  MainScreen(navController = rememberNavController(), viewModel = viewModel())
}