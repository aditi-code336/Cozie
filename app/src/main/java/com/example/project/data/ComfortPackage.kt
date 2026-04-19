package com.example.project.data

sealed class ComfortPackage {
    abstract val beverage: String
    abstract val chocolate: String
    abstract val media: String
    abstract val rationale: String

    data class Default(
        override val beverage: String,
        override val chocolate: String,
        override val media: String,
        override val rationale: String
    ) : ComfortPackage()

    data class Personalized(
        override val beverage: String,
        override val chocolate: String,
        override val media: String,
        override val rationale: String,
        val feedbackScore: Int
    ) : ComfortPackage()
}

enum class TimePeriod {
    Morning, Afternoon, Evening
}
