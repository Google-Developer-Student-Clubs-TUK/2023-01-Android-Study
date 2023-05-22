package com.example.ktorpractice.pages

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
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
import com.example.ktorpractice.KtorClient
import io.ktor.client.request.get
import io.ktor.client.statement.readBytes
import io.ktor.http.Url
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayInputStream

@Composable
fun ImageApiPage(api: String) {
    var inputFieldData by remember { mutableStateOf(0) }
    var buttonEnabled by remember { mutableStateOf(true) }
    val bitmapImages = remember { mutableListOf<Bitmap>() }

    Box(modifier = Modifier.fillMaxSize()) {
        Column (Modifier.fillMaxSize()){
            Button(onClick = {inputFieldData++}, enabled = buttonEnabled) {
                Text("add new image")
            }
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                items (bitmapImages.size) { index ->
                    Image(bitmap = bitmapImages[index].asImageBitmap(), contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f))
                }
            }
        }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            Text("This Page use api $api")
        }
    }

    LaunchedEffect(inputFieldData) {
        buttonEnabled = false
        val newBitmap = getBitmap(Uri.parse(api))
        newBitmap?.let {
            bitmapImages.add(it)
        }
        buttonEnabled = true
    }
}

// https://stackoverflow.com/questions/16324482/convert-uri-to-string-and-string-to-uri
suspend fun getBitmap(uri: Uri): Bitmap? {
    return runCatching {
        val byteArray = withContext(Dispatchers.IO) {
            KtorClient.client.get(Url(uri.toString())).readBytes()
        }

        return withContext(Dispatchers.Default) {
            ByteArrayInputStream(byteArray).use {
                val option = BitmapFactory.Options()
                option.inPreferredConfig = Bitmap.Config.RGB_565 // To save memory, or use RGB_8888 if alpha channel is expected
                BitmapFactory.decodeStream(it, null, option)
            }
        }
    }.getOrElse {
        Log.e("getBitmap", "Failed to get bitmap ${it.message ?: ""}" )
        null
    }
}
