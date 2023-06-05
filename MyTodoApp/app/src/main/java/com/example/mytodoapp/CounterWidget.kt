package com.example.mytodoapp

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.*
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider

object CounterWidget: GlanceAppWidget() {
    override val sizeMode = SizeMode.Single
    val TODO_LIST_ID = ActionParameters.Key<Int>("todo_list_id")

    val jsonTodoList = stringPreferencesKey("json")

    @Composable
    override fun Content() {
        val data = currentState(key = jsonTodoList) ?: ""
        val dataList = remember(data) {
            data.split("\n").filter { it.isNotEmpty() }
        }
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(Color.DarkGray),
            verticalAlignment = Alignment.Vertical.CenterVertically,
            horizontalAlignment = Alignment.Horizontal.CenterHorizontally
        ) {
            ContentTitle()
            LazyColumn(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .background(Color.LightGray)
            ) {
                items (count = dataList.size) {
                    ContentColumnItem(index = it, text = dataList[it])
                }
            }
        }
    }
    @Composable
    fun ContentTitle () {
        Box(
            modifier = GlanceModifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Box(
                modifier = GlanceModifier.fillMaxSize(),
                contentAlignment = Alignment.CenterStart) {
                Text(
                    text = "My Todo App",
                    style = TextStyle(
                        fontWeight = FontWeight.Medium,
                        color = ColorProvider(Color.White),
                        fontSize = 26.sp
                    )
                )
            }
            Box(
                modifier = GlanceModifier.fillMaxSize(),
                contentAlignment = Alignment.CenterEnd) {
                Button(text = "Refresh",
                    onClick = actionRunCallback<TodoRefreshActionCallback>()
                )
            }
        }
    }
    @Composable
    fun ContentColumnItem (index: Int, text: String) {
        Box(
            modifier = GlanceModifier
                .height(40.dp)
                .fillMaxWidth()
                .background(Color.White)
                .padding(1.dp)
                .cornerRadius(3.dp)
        ) {
            Box(
                modifier = GlanceModifier.fillMaxSize(),
                contentAlignment = Alignment.CenterStart) {
                Text(text,
                    style = TextStyle(
                        fontWeight = FontWeight.Medium,
                        color = ColorProvider(Color.Black),
                        fontSize = 16.sp
                    ))
            }
            Box(
                modifier = GlanceModifier.fillMaxSize(),
                contentAlignment = Alignment.CenterEnd) {
                Button(
                    text = "✓",
                    onClick = actionRunCallback<TodoClearActionCallback>(
                        actionParametersOf(TODO_LIST_ID to index)
                    ))
            }
        }
    }
}

class SimpleCounterWidgetReceiver: GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = CounterWidget
}
class TodoRefreshActionCallback: ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val v = TodoList.getString(context)
        updateAppWidgetState(context, glanceId) { prefs ->
            prefs[CounterWidget.jsonTodoList] = v
            CounterWidget.update(context, glanceId)
        }
        updateAppWidgetState(context, glanceId) {
            CounterWidget.update(context, glanceId)
        }
    }
}
class TodoClearActionCallback: ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val todoId = parameters.getOrDefault(CounterWidget.TODO_LIST_ID, 0)
        TodoList.remove(context, todoId)
        val v = TodoList.getString(context)

        updateAppWidgetState(context, glanceId) { prefs ->
            prefs[CounterWidget.jsonTodoList] = v
            CounterWidget.update(context, glanceId)
        }
        updateAppWidgetState(context, glanceId) {
            CounterWidget.update(context, glanceId)
        }
    }
}