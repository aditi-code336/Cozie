package com.example.project.data

val recommendations = listOf(
    ComfortRecommendation(
        mood = "The Monsoon Soul", // Tired equivalent
        periodDayRange = 1..3,
        chocolateType = "Dark Chocolate with Sea Salt",
        beverageType = "Strong Cutting Chai",
        movieGenre = "The Lunchbox (Comforting/Realistic)",
        songVibe = "Lo-fi Mumbai Rain Ambience"
    ),
    ComfortRecommendation(
        mood = "The Chai Chaser", // Irritated equivalent
        periodDayRange = 4..10,
        chocolateType = "Crunchy Hazelnut Milk Chocolate",
        beverageType = "Iced Kokum Sharbat (Cooling)",
        movieGenre = "Zindagi Na Milegi Dobara (Escapist)",
        songVibe = "High-energy Bollywood Classics"
    ),
    ComfortRecommendation(
        mood = "The Gond Guardian",
        periodDayRange = 1..31,
        chocolateType = "Artisan Jaggery Chocolate",
        beverageType = "Spiced Filter Coffee",
        movieGenre = "Tumbbad (Visual Masterpiece)",
        songVibe = "Indian Classical Fusion"
    )
)

fun provideComfort(mood: String, day: Int): ComfortRecommendation {
    return recommendations.find { 
        it.mood.equals(mood, ignoreCase = true) && day in it.periodDayRange 
    } ?: ComfortRecommendation(
        mood = mood,
        periodDayRange = 1..31,
        chocolateType = "Any chocolate you love!",
        beverageType = "Your favorite cozy drink",
        movieGenre = "Something you've seen a hundred times",
        songVibe = "Whatever makes you smile"
    )
}
