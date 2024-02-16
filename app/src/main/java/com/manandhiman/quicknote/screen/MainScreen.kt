package com.manandhiman.quicknote.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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

    LazyColumn(
      Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {

      items(viewModel.notes.value.size) { index ->
        PostUI(viewModel.notes.value[index], index, navController)
      }
    }
  }
}

@Composable
fun PostUI(note: Note, index: Int, navController: NavHostController) {
  Column(
    Modifier.clickable { navController.navigate("note/$index") } // clicking item takes to note detail
  ) {
    Text(text = note.title, fontSize = 24.sp)
    Divider()
    Spacer(modifier = Modifier.height(8.dp))
  }

}

@Preview(showBackground = true)
@Composable
fun Prev() {
  MainScreen(navController = rememberNavController(), viewModel = viewModel())
}