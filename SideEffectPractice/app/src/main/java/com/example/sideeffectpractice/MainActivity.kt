package com.example.sideeffectpractice

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val loremIpsum = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."


        super.onCreate(savedInstanceState)
        setContent {
            var page by remember {
                mutableStateOf(0)
            };
            val pageCount = 6;

            var ai by remember { mutableStateOf(0) }
            var someColor by remember { mutableStateOf(Color.Black) }

            Box (modifier = Modifier.fillMaxSize()) {
                when(page) {
                    0 -> {
                        SamplePage()
                    }
                    1 -> {
                        DelayedIpsum(loremIpsum)
                    }
                    2 -> {
                        ResumeableIpsum(lorem = loremIpsum, abort = { index ->
                            ai = index
                        }, index = ai);
                    }
                    3 -> {
                        Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Top) {
                            ModifiableIpsum(loremIpsum, color = someColor)
                            Button(onClick = {someColor = randomColor() }) {
                                Text("recolor text")
                            }
                        }
                    }
                    4 -> {
                        DelayedIpsumbutRedWith('e', lorem = loremIpsum);
                    }
                    5 -> {
                        DelayedIpsumWithButtons(lorem = loremIpsum)
                    }
                }

                Box (modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        LeftButton( { page-- }, enabled = page > 0);
                        FrameCounter()
                        RightButton( {page++ }, enabled = page < pageCount - 1);
                    }
                }
            }
        }
    }
}
@Composable
fun SamplePage() {
    Text("hello, world!");
}

@Composable
fun DelayedIpsum(lorem: String) {
    var text by remember { mutableStateOf("") }
    Text(text);

    LaunchedEffect(Unit) {
        val list = lorem.split(' ');

        for (word in list) {
            text += word;
            delay(500)
            text += " ";
        }
    }
}
@Composable
fun ResumeableIpsum(lorem: String, abort: (Int) -> Unit, index: Int = 0) {
    var text by remember { mutableStateOf("") }
    var process by remember { mutableStateOf(0) }
    Text(text);

    LaunchedEffect(Unit) {
        val list = lorem.split(' ');

        for (i in 0 until index) {
            text += list[i];
            text += " ";
        }
        for (i in index until list.count()) {
            process = i;

            text += list[i];
            delay(500)
            text += " ";
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            abort(process);
        }
    }

}

@Composable
fun ModifiableIpsum(lorem: String, color: Color = Color.Black) {
    val updatedModifier by rememberUpdatedState(color)
    var text by remember { mutableStateOf("") }
    Text(text, color = color);

    LaunchedEffect(Unit) {
        val list = lorem.split(' ');

        for (word in list) {
            text += word;
            delay(500)
            text += " ";
        }
    }
}

@Composable
fun DelayedIpsumbutRedWith(e: Char, lorem: String) {
    var text by remember { mutableStateOf("") }

    var wordNow by remember { mutableStateOf("") }
    val isRed by remember {
        derivedStateOf { wordNow.startsWith(e) }
    }
    Text(text, color = if (isRed) Color.Red else Color.Black);

    LaunchedEffect(Unit) {
        val list = lorem.split(' ');

        for (word in list) {
            wordNow = word;
            text += word;
            delay(500)
            text += " ";
        }
    }
}

@Composable
fun DelayedIpsumWithButtons(lorem: String) {
    var text by remember { mutableStateOf("") }
    var coroutine = rememberCoroutineScope();

    var stopButtonPressed by remember { mutableStateOf(false) }
    var startButtonPressed by remember { mutableStateOf(false) }
    Column(Modifier.fillMaxSize()) {
        Text(text);
        Row(Modifier.fillMaxWidth()) {
            Button(onClick = {coroutine.cancel(); stopButtonPressed = true;},
                enabled = startButtonPressed && !stopButtonPressed) {
                Icon(Icons.Default.Clear, contentDescription = null)
            }
            Button(onClick = {coroutine.launch {
                startButtonPressed = true;
                val list = lorem.split(' ');

                for (word in list) {
                    text += word;
                    delay(500)
                    text += " ";
                }
            }}, enabled = !startButtonPressed) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        }
    }
}
@Composable
fun FrameCounter() {
    var recompositions by remember { mutableStateOf(0) };
    Text("recompositions: $recompositions");

    SideEffect {
    }
}

@Composable
fun LeftButton(onClick: () -> Unit, enabled: Boolean = true) {
    Button(onClick = onClick, enabled = enabled) {
        Icon(Icons.Default.ArrowBack, contentDescription = null)
    }
}

@Composable
fun RightButton(onClick: () -> Unit, enabled: Boolean = true) {
    Button(onClick = onClick, enabled = enabled) {
        Icon(Icons.Default.ArrowForward, contentDescription = null)
    }
}

fun randomColor(): Color {
    return Color(
        alpha = 1.0f,
        red = Random.nextFloat(),
        green = Random.nextFloat(),
        blue = Random.nextFloat()
    )
}