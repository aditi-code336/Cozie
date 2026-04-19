package com.example.project.ui

import androidx.lifecycle.ViewModel
import com.example.project.data.ComfortPackage
import com.example.project.data.TimePeriod
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Calendar
import kotlin.random.Random

class OracleViewModel : ViewModel() {
    private val _comfortPackage = MutableStateFlow<ComfortPackage?>(null)
    val comfortPackage: StateFlow<ComfortPackage?> = _comfortPackage.asStateFlow()

    private val recommendationsMap = mapOf(
        "Spicy Rickshaw" to mapOf(
            TimePeriod.Morning to listOf(
                ComfortPackage.Default("Cold Brew Coffee", "Extra Dark Chocolate", "Fast-paced Techno", "High caffeine to match your high irritability."),
                ComfortPackage.Default("Iced Americano", "Sea Salt Dark Chocolate", "Action Movie Trailers", "Sharp flavors to cut through the noise.")
            ),
            TimePeriod.Afternoon to listOf(
                ComfortPackage.Default("Cutting Chai", "Chili Chocolate", "Stand-up Comedy", "Spicy kick for the afternoon traffic jam."),
                ComfortPackage.Default("Ginger Ale", "Spiced Almonds", "Fast-paced Podcast", "Zesty refresh for a heated afternoon.")
            ),
            TimePeriod.Evening to listOf(
                ComfortPackage.Default("Strong Assam Tea", "Bitter Cocoa Nib", "Action Thriller", "Strong flavors to wind down the rage.")
            )
        ),
        "Monsoon Cloud" to mapOf(
            TimePeriod.Morning to listOf(
                ComfortPackage.Default("Earl Grey Tea", "Lavender Chocolate", "Acoustic Folk", "Gentle start for a sensitive morning.")
            ),
            TimePeriod.Afternoon to listOf(
                ComfortPackage.Default("Hot Cocoa", "Caramel Bar", "Coming-of-age Movies", "Dopamine boost for the afternoon slump."),
                ComfortPackage.Default("Matcha Latte", "White Chocolate with Berries", "Studio Ghibli Soundtracks", "Soft colors and soft flavors.")
            ),
            TimePeriod.Evening to listOf(
                ComfortPackage.Default("Chamomile Tea", "Milk Chocolate", "Studio Ghibli Movie", "Max comfort for your inner main character.")
            )
        ),
        "Zen Pavilion" to mapOf(
            TimePeriod.Morning to listOf(
                ComfortPackage.Default("White Tea", "Honey Chocolate", "Ambient Nature Sounds", "Minimal mental load for a peaceful start.")
            ),
            TimePeriod.Afternoon to listOf(
                ComfortPackage.Default("Peppermint Tea", "Matcha Chocolate", "Satisfying ASMR", "Cooling refresh for a calm afternoon.")
            ),
            TimePeriod.Evening to listOf(
                ComfortPackage.Default("Lavender Tea", "White Chocolate", "Nature Documentaries", "Lowering cortisol for better sleep.")
            )
        ),
        "Brain Fog" to mapOf(
            TimePeriod.Morning to listOf(
                ComfortPackage.Default("Extra Strong Filter Coffee", "Wafer Bar", "Lo-fi Beats", "Simple carbs and caffeine to jumpstart the brain.")
            ),
            TimePeriod.Afternoon to listOf(
                ComfortPackage.Default("Double Espresso", "Glucose Biscuit", "90s Sitcoms", "Automatic comfort for the cognitive buffering.")
            ),
            TimePeriod.Evening to listOf(
                ComfortPackage.Default("Sweet Milky Tea", "Caramel Wafer", "Nonsense Humor Videos", "Zero effort required for your tired brain.")
            )
        )
    )

    fun updateMood(mood: String) {
        val timePeriod = getCurrentTimePeriod()
        val packages = recommendationsMap[mood]?.get(timePeriod) ?: listOf(
            ComfortPackage.Default("Your favorite drink", "Your favorite chocolate", "A comforting classic", "Stay cozy!")
        )
        _comfortPackage.value = packages[Random.nextInt(packages.size)]
    }

    private fun getCurrentTimePeriod(): TimePeriod {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when (hour) {
            in 5..11 -> TimePeriod.Morning
            in 12..17 -> TimePeriod.Afternoon
            else -> TimePeriod.Evening
        }
    }
}
