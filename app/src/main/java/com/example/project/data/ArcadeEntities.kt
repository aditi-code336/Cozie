package com.example.project.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pending_cravings")
data class PendingCraving(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val craving: String,
    val timestamp: Long
)

@Entity(tableName = "small_wins")
data class SmallWin(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val winType: String,
    val timestamp: Long
)
