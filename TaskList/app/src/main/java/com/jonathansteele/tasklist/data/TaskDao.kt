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
package com.jonathansteele.tasklist.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import kotlin.collections.List as KList

@Dao
interface TaskDao {
    @Query("SELECT * from task WHERE listId=:listId AND hidden=0")
    fun getTasks(listId: Int): Flow<KList<Task>>

    @Query("SELECT * from task WHERE taskId=:id")
    fun getTask(id: Int): Flow<Task>

    @Query("SELECT name from task WHERE completedDate='0' ORDER BY completedDate DESC LIMIT :limit")
    fun getTopTaskNames(limit: Int): Flow<KList<String>>

    @Insert
    suspend fun insertTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateTask(task: Task): Int
}
