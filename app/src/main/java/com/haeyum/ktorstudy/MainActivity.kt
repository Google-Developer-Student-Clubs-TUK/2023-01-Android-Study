package com.haeyum.ktorstudy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.haeyum.ktorstudy.ui.theme.KtorStudyTheme
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var username by remember {
                mutableStateOf("")
            }

            var response by remember {
                mutableStateOf<User?>(null)
            }

            var isSearching by remember {
                mutableStateOf(false)
            }

            val coroutineScope = rememberCoroutineScope()

            Column {
                Row {
                    TextField(value = username, onValueChange = {
                        username = it
                    })

                    Button(
                        onClick = {
                            coroutineScope.launch {
                                response = null
                                isSearching = true
                                response = runCatching {
                                    KtorClient.client.get("https://api.github.com/users/$username")
                                        .body<User>()
                                }.getOrNull()
                                isSearching = false
                            }
                        }
                    ) {
                        Text(text = "Search")
                    }
                }


                response?.let {
                    Column {
                        Text(text = it.name)
                        Text(text = it.bio)
                        Text(text = "Follower: ${it.followers}")
                        Text(text = "Following: ${it.following}")
                        AsyncImage(
                            model = it.avatar_url,
                            contentDescription = "Avatar URL",
                            modifier = Modifier.clip(CircleShape)
                        )
                    }
                } ?: if (isSearching) CircularProgressIndicator() else Unit
            }
        }
    }
}