package com.example.project.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "comfort_logs",
    foreignKeys = [
        ForeignKey(
            entity = MoodEntry::class,
            parentColumns = ["id"],
            childColumns = ["parentMoodId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ComfortLog(
    @PrimaryKey(autoGenerate = true) val logId: Long = 0,
    val parentMoodId: Long,
    val chocolateGiven: String,
    val beverageGiven: String,
    val movieGiven: String,
    val userRating: Int // 1-5
)
