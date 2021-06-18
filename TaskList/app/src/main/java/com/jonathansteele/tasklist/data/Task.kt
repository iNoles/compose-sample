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

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(
    @PrimaryKey(autoGenerate = true) val taskId: Int = 0,
    @ColumnInfo val listId: Int,
    @ColumnInfo val name: String = "",
    @ColumnInfo val notes: String = "",
    @ColumnInfo var completedDate: String = "0",
    @ColumnInfo val hidden: Boolean = false
) {
    fun getCompletedDateMillis() = completedDate.toLong()
}
