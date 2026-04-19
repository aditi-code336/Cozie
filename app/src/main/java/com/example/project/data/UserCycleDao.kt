package com.example.project.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserCycleDao {
    @Query("SELECT * FROM user_cycle_entries ORDER BY date DESC")
    fun getAllEntries(): Flow<List<UserCycleEntry>>

    @Query("SELECT * FROM user_cycle_entries WHERE notes LIKE '%' || :searchQuery || '%' OR moodId LIKE '%' || :searchQuery || '%' ORDER BY date DESC")
    fun searchEntries(searchQuery: String): Flow<List<UserCycleEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: UserCycleEntry): Long

    @Query("DELETE FROM user_cycle_entries WHERE id = :id")
    suspend fun deleteEntryById(id: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFeedback(feedback: UserFeedback)

    @Query("""
        SELECT moodId as mood, COUNT(*) as count 
        FROM user_cycle_entries 
        GROUP BY moodId 
        ORDER BY count DESC
    """)
    fun getMoodFrequency(): Flow<List<MoodFrequency>>

    @Query("SELECT * FROM user_cycle_entries WHERE date >= :sinceTimestamp ORDER BY date ASC")
    fun getEntriesSince(sinceTimestamp: Long): Flow<List<UserCycleEntry>>
}
