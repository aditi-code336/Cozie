package com.example.project.util

import android.content.Context
import android.content.Intent

object BroadcastGenerator {
    /**
     * Generates a stylized text broadcast and opens the system share sheet.
     */
    fun shareMoodAlert(
        context: Context,
        userName: String = "Fam",
        moodName: String,
        day: Int,
        chocolateType: String
    ) {
        val message = "🚨 COZIE ALERT: $userName is currently a $moodName on Day $day. " +
                "Recommended Intervention: $chocolateType and zero questions. 🍫☕"

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, message)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, "Broadcast Your Vibe")
        context.startActivity(shareIntent)
    }
}
