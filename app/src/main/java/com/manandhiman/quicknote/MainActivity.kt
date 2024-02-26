package com.manandhiman.quicknote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.manandhiman.quicknote.database.DatabaseHandler
import com.manandhiman.quicknote.ui.screen.MainScreen
import com.manandhiman.quicknote.ui.screen.NoteScreen
import com.manandhiman.quicknote.ui.theme.QuickNoteTheme
import com.manandhiman.quicknote.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      QuickNoteTheme {

        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {

          val db = DatabaseHandler(applicationContext)

          val viewModel = viewModel { MainViewModel(db) }

          val navController = rememberNavController()
          NavHost(navController, startDestination = "main", modifier = Modifier) {
            composable(route = "main") {
              MainScreen(navController, viewModel)
            }
            composable(route = "note/{index}") {
              NoteScreen(navController, viewModel, it.arguments?.getString("index")!!.toInt())
            }
          }

        }
      }
    }
  }
}