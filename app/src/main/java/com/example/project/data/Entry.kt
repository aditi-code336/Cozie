package com.example.project.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "entries")
data class Entry(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: Long,
    val day: String,
    val mood: String,
    val beverage: String,
    val snack: String,
    val rating: Int
)
