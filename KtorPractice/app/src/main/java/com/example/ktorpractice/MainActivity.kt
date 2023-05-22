package com.example.ktorpractice

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ktorpractice.KtorClient.client
import com.example.ktorpractice.pages.ImageApiPage
import com.example.ktorpractice.pages.JsonApiPage
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.readBytes
import io.ktor.http.Url
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayInputStream

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            Column (Modifier.fillMaxSize()){

                NavHost(navController = navController, startDestination = "text", modifier = Modifier.fillMaxHeight(0.9f)) {
                    composable("text") { JsonApiPage("https://random-data-api.com/api/v2/beers") }
                    composable("image") { ImageApiPage("https://picsum.photos/200/200") }
                }
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
                    Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                        Button(onClick = { navController.navigate("text")}) {
                            Text("text")
                        }
                        Button(onClick = { navController.navigate("image")}) {
                            Text("image")
                        }
                    }
                }
            }
        }
    }
}


