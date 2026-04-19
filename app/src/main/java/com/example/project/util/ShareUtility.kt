package com.example.project.util

import android.content.Context
import android.content.Intent
import android.widget.Toast

object ShareUtility {
    fun shareStatus(context: Context, mood: String, chocolate: String) {
        val message = "Current Status: $mood. Recommended Action: $chocolate. Thanks for understanding!"
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, message)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, "Share Status")
        context.startActivity(shareIntent)
    }

    fun shareToWhatsApp(context: Context, mood: String, chocolate: String) {
        val message = "Current Status: $mood. Recommended Action: $chocolate. Thanks for understanding!"
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, message)
            type = "text/plain"
            setPackage("com.whatsapp")
        }
        
        try {
            context.startActivity(sendIntent)
        } catch (e: Exception) {
            Toast.makeText(context, "WhatsApp not installed. Opening general share.", Toast.LENGTH_SHORT).show()
            shareStatus(context, mood, chocolate)
        }
    }
}
