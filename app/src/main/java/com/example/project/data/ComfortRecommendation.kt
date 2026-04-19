package com.example.project.data

data class ComfortRecommendation(
    val mood: String,
    val periodDayRange: IntRange,
    val chocolateType: String,
    val beverageType: String,
    val movieGenre: String,
    val songVibe: String
)
