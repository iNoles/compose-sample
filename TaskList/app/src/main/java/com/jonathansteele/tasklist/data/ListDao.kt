package com.jonathansteele.tasklist.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kotlin.collections.List as KList

@Dao
interface ListDao {
    @Query("SELECT * from list")
    fun getLists(): Flow<KList<List>>

    @Insert
    suspend fun insertList(list: List)
}