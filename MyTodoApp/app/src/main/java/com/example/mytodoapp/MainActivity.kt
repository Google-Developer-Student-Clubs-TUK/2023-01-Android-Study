package com.example.mytodoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.mytodoapp.ui.theme.MyTodoAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTodoAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TodoItemColumn()
                }
            }
        }
    }
}
@Composable
fun TodoItemColumn() {
    val context = LocalContext.current
    var data by remember {
        mutableStateOf(listOf<String>())
    }
    var recentlyRemoved by remember {
        mutableStateOf<Int?>(null)
    }
    var recentlyAdded by remember {
        mutableStateOf<String?>(null)
    }

    LazyColumn {
        item {
            TodoNewTextRow(onSubmit = {
                recentlyAdded = it
            })
        }
        items(count = data.size) {
            TodoRow(index = it, text = data[it], onClick =  {
                recentlyRemoved = it
            })
        }
    }
    LaunchedEffect(Unit) {
        TodoList.init(context)
        data = TodoList.get(context).toMutableList()
    }
    LaunchedEffect(recentlyRemoved) {
        if (recentlyRemoved != null) {
            TodoList.remove(context, recentlyRemoved!!)
            recentlyRemoved = null
            data = TodoList.get(context)

        }
    }
    LaunchedEffect(recentlyAdded) {
        if (recentlyAdded != null) {
            TodoList.add(context, recentlyAdded!!)

            recentlyAdded = null
            data = TodoList.get(context)
        }
    }
}
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TodoNewTextRow(onSubmit: (newString: String) -> Unit) {
    var newString by remember { mutableStateOf("") }
    Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceEvenly) {
        TextField(
            value = newString,
            onValueChange = { newString = it },
            label = { Text("New String") }
        )
        Button(onClick = {
            onSubmit(newString)
            newString = ""
        }) {
            Text(if (newString == "") "Refresh" else "Add")
        }
    }
}
@Composable
fun TodoRow(index: Int, text: String, onClick: () -> Unit) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text("$index: $text")
        Button(onClick = onClick) {
            Text("✓")
        }
    }
}