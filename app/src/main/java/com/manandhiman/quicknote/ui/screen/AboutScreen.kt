package com.manandhiman.quicknote.ui.screen

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.manandhiman.quicknote.R

@Composable
fun AboutScreen(
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
        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Go Back", Modifier.clickable { popBackStack() })
      }
    }
  ) {

    Column(
      Modifier
        .fillMaxSize()
        .padding(8.dp + it.calculateTopPadding())
    ) {

      val context = LocalContext.current

      Text(text = stringResource(id = R.string.about), fontSize = 24.sp)

      Spacer(modifier = Modifier.height(64.dp))

      Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Absolute.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Image(
          painter = painterResource(id = R.drawable.logo_github),
          contentDescription = "Github Logo",
          modifier = Modifier
            .size(80.dp)
            .clickable {
              try {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("https://github.com/manandhiman/quick-note")
                context.startActivity(intent)
              } catch (e: Exception) {
                Toast
                  .makeText(context, "Some Error Occurred. Please Try Again", Toast.LENGTH_LONG)
                  .show()
              }
            }
        )

        Image(
          painter = painterResource(id = R.drawable.logo_linkedin),
          contentDescription = "LinkedIn Logo",
          modifier = Modifier
            .size(80.dp)
            .clickable {
              try {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("https://www.linkedin.com/in/manan-dhiman/")
                context.startActivity(intent)
              } catch (e: Exception) {
                Toast
                  .makeText(context, "Some Error Occurred. Please Try Again", Toast.LENGTH_LONG)
                  .show()
              }
            }
        )
      }

    }

  }

}

@Preview(showBackground = true)
@Composable
private fun Prev() {
  AboutScreen({})
}