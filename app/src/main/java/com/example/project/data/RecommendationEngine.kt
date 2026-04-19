package com.example.project.data

enum class MoodType {
    Sensitive, Irritable, Calm, Distracted
}

class RecommendationEngine {
    fun fetchComfort(mood: MoodType, hour: Int): ComfortPackage {
        return when (mood) {
            MoodType.Sensitive -> ComfortPackage.Default(
                beverage = "Hot Ginger Chai",
                chocolate = "Sea Salt Dark Chocolate",
                media = "Slice of Life Animation",
                rationale = "Ginger helps reduce inflammation and soothe the stomach, while sea salt dark chocolate provides a grounding magnesium boost."
            )
            MoodType.Irritable -> ComfortPackage.Default(
                beverage = "Cold Kokum Juice",
                chocolate = "Almond Crunch Chocolate",
                media = "High-Pace Thriller",
                rationale = "Kokum is cooling and refreshing, helping to lower body heat often associated with irritability. The crunch of almonds provides a satisfying sensory outlet."
            )
            MoodType.Calm -> ComfortPackage.Default(
                beverage = "Kashmiri Kahwa",
                chocolate = "Plain Milk Chocolate",
                media = "Instrumental Jazz",
                rationale = "Kashmiri Kahwa with saffron and spices promotes well-being and maintains your current state of tranquility."
            )
            MoodType.Distracted -> ComfortPackage.Default(
                beverage = "Strong Filter Coffee",
                chocolate = "Wafer Chocolate",
                media = "20-minute Sitcom",
                rationale = "Caffeine provides a focused boost for cognitive clarity, while a light wafer snack and short sitcom offer a structured break without overwhelming your focus."
            )
        }
    }
}
