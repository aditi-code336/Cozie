package com.example.project.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface EntryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: Entry)

    @Query("SELECT * FROM entries WHERE date >= :sinceDate ORDER BY date DESC")
    fun getLast30DaysEntries(sinceDate: Long): Flow<List<Entry>>

    @Query("SELECT * FROM entries ORDER BY date DESC")
    fun getAllEntries(): Flow<List<Entry>>
}
