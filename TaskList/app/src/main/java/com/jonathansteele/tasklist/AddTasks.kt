/*
 * Copyright 2021 Jonathan Steele
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package com.jonathansteele.tasklist

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.jonathansteele.tasklist.data.AppDatabase
import com.jonathansteele.tasklist.data.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@Composable
fun AddOrEditTask(editMode: Boolean = false, taskId: Int = -1, onBack: () -> Unit) {
    val context = LocalContext.current
    val database = AppDatabase.getInstance(context)

    val tasks = database.taskDao().getTask(taskId)
    val task = tasks.collectAsState(initial = null).value

    val name = remember { mutableStateOf("") }
    val notes = remember { mutableStateOf("") }
    val list = remember { mutableStateOf(1) }
    val isOpen = remember { mutableStateOf(false) } // initial value

    if (editMode) {
        name.value = task?.name.toString()
        notes.value = task?.notes.toString()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Task List") },
                actions = {
                    IconButton(onClick = {
                        saveToDatabase(
                            database, editMode, taskId,
                            name.value, notes.value, list.value
                        )
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = "Save to DB")
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Going Back to Home")
                    }
                }
            )
        },
    ) {
        Column {
            DropDownList(
                requestToOpen = isOpen.value,
                list = populateList(),
                request = { isOpen.value = it },
                selectedInt = { list.value = it }
            )
            Spacer(
                modifier = Modifier
                    .size(10.dp, 10.dp)
                    .clickable(
                        onClick = { isOpen.value = true }
                    )
            )
            TextField(
                value = name.value,
                onValueChange = { name.value = it },
                label = { Text(text = "Name") }
            )
            TextField(
                value = notes.value,
                onValueChange = { notes.value = it },
                label = { Text("Notes") },
                singleLine = true
            )
        }
    }
}

fun saveToDatabase(
    database: AppDatabase,
    editMode: Boolean,
    taskId: Int,
    name: String,
    notes: String,
    listId: Int
) {
    val task = when (editMode) {
        false -> Task(listId = listId, name = name, notes = notes)
        true -> Task(taskId, listId, name, notes)
    }

    val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    scope.launch {
        if (editMode) {
            val update = database.taskDao().updateTask(task)
            Log.d("AddTasks - Update", update.toString())
        } else {
            database.taskDao().insertTask(task)
        }
    }
}
