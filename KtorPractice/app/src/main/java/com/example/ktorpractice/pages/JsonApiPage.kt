package com.example.ktorpractice.pages

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ktorpractice.KtorClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.Serializable

@Composable
fun JsonApiPage (api: String) {
    var data by remember { mutableStateOf<Beer?>(null) }
    Column (modifier = Modifier.fillMaxSize()) {
        data?.let {
            Text(it.name ?: "", fontSize = 40.sp)
        }

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            Text("This Page use api $api")
        }
    }
    LaunchedEffect(Unit) {
        data = getJson(Uri.parse(api))
    }
}

suspend fun getJson(uri: Uri): Beer? {
    return try {
        KtorClient.client
            .get(uri.toString())
            .body<Beer>()
    }
    catch (e: Exception){
        Log.e("getJson", "Failed to get Json ${e.message ?: ""}")
        null
    }
}

@Serializable
data class Beer (
    var id      : Int?    = null,
    var uid     : String? = null,
    var brand   : String? = null,
    var name    : String? = null,
    var style   : String? = null,
    var hop     : String? = null,
    var yeast   : String? = null,
    var malts   : String? = null,
    var ibu     : String? = null,
    var alcohol : String? = null,
    var blg     : String? = null
)