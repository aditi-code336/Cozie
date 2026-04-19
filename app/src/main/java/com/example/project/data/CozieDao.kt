package com.example.project.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface CozieDao {
    @Insert
    suspend fun insertMood(mood: MoodEntry): Long

    @Insert
    suspend fun insertComfortLog(log: ComfortLog)

    @Transaction
    suspend fun insertMoodWithLog(mood: MoodEntry, chocolate: String, beverage: String, movie: String) {
        val moodId = insertMood(mood)
        val log = ComfortLog(
            parentMoodId = moodId,
            chocolateGiven = chocolate,
            beverageGiven = beverage,
            movieGiven = movie,
            userRating = 0 // Initial rating
        )
        insertComfortLog(log)
    }

    @Query("SELECT * FROM mood_entries ORDER BY timestamp DESC")
    fun getAllMoodEntries(): Flow<List<MoodEntry>>

    @Query("SELECT * FROM comfort_logs WHERE parentMoodId = :moodId")
    suspend fun getLogForMood(moodId: Long): ComfortLog?

    @Query("UPDATE comfort_logs SET userRating = :rating WHERE logId = :logId")
    suspend fun updateRating(logId: Long, rating: Int)

    @Query("SELECT * FROM user_stats WHERE id = 1")
    fun getUserStats(): Flow<UserStats?>

    @Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    suspend fun updateStats(stats: UserStats)

    @Insert
    suspend fun insertPendingCraving(craving: PendingCraving)

    @Insert
    suspend fun insertSmallWin(win: SmallWin)
}
