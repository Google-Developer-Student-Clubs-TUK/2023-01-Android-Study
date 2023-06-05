package com.example.mytodoapp

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

// At the top level of your kotlin file:
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "data")

object TodoList {
    private val example_counter = stringPreferencesKey("example_counter")

    suspend fun add(context: Context, str: String) {
        val data = get(context).toMutableList()
        data.add(str)
        set(context, data)
    }

    suspend fun remove(context: Context, i: Int) {
        val data = get(context).toMutableList()
        data.removeAt(i)
        set(context, data)
    }

    suspend fun getString(context: Context): String {
        return context.dataStore.data.first()[example_counter] ?: ""
    }

    suspend fun get(context: Context): List<String> {
        return getString(context).split("\n").filter { it.isNotEmpty() }
    }

    suspend fun set(context: Context, list: List<String>) {
        context.dataStore.edit { data ->
            data[example_counter] = list.joinToString(separator = "\n")
        }
    }

    suspend fun init(context: Context) {
        val sampleData = getString(context)
        if (sampleData != "") return

        set(context, listOf("밥 먹기", "세수 하기", "학교 가기"))
    }
}