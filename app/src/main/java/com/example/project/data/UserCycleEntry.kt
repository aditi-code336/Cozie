package com.example.project.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_cycle_entries")
data class UserCycleEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: Long,
    val moodId: String,
    val cycleDay: Int,
    val chocolateRating: Int = 0, // 1-5 rating for the chocolate recommendation
    val notes: String = ""
)

@Entity(tableName = "user_feedback")
data class UserFeedback(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val entryId: Int,
    val mood: String,
    val beverage: String,
    val chocolate: String,
    val media: String,
    val efficacyRating: Int // 1-5
)
