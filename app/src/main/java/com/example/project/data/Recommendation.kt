package com.example.project.data

data class Recommendation(
    val mood: String,
    val beverage: String,
    val snack: String,
    val media: String,
    val rationale: String
)

object RecommendationProvider {
    val recommendations = listOf(
        Recommendation(
            mood = "Tired",
            beverage = "Strong Ginger Chai",
            snack = "Dark Chocolate (70%)",
            media = "Relaxing Lo-fi / Jazz",
            rationale = "Caffeine + Magnesium for a gentle boost."
        ),
        Recommendation(
            mood = "Happy",
            beverage = "Iced Lemon Tea",
            snack = "Milk Chocolate Fruit/Nut",
            media = "Upbeat Sitcoms",
            rationale = "Refreshing sugar to maintain momentum."
        ),
        Recommendation(
            mood = "Sad",
            beverage = "Warm Turmeric Latte",
            snack = "Caramel Sea Salt Bar",
            media = "Studio Ghibli Film",
            rationale = "Comforting warmth + serotonin boost."
        ),
        Recommendation(
            mood = "Angry",
            beverage = "Mint & Lime Soda",
            snack = "Crunchy Almond Bar",
            media = "High-Pace Action Thriller",
            rationale = "Cooling mint + crunch to vent frustration."
        ),
        Recommendation(
            mood = "Anxious",
            beverage = "Chamomile / Kahwa",
            snack = "Plain Milk Chocolate",
            media = "Nature Documentary",
            rationale = "L-Theanine + low-stimulation visuals"
        )
    )

    fun getRecommendationForMood(uiMood: String): Recommendation {
        val targetMood = when (uiMood) {
            "Monsoon Cloud" -> "Sad"
            "Spicy Rickshaw" -> "Angry"
            "Zen Pavilion" -> "Happy"
            "Brain Fog" -> "Tired"
            else -> "Anxious"
        }
        return recommendations.find { it.mood == targetMood } ?: recommendations[0]
    }
}
