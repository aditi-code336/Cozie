package com.example.project.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_stats")
data class UserStats(
    @PrimaryKey val id: Int = 1, // Single row for stats
    val mostEffectiveChocolate: String,
    val mostEffectiveBeverage: String,
    val mostEffectiveMovie: String,
    val averageRating: Double
)
