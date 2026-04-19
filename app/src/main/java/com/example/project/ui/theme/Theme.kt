package com.example.project.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = ElectricPurple,
    secondary = SoftPurple,
    surface = DeepSpace,
    tertiary = RoyalPurple,
    background = NightBlack,
    onPrimary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
    primaryContainer = MutedPurple,
    onPrimaryContainer = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = RoyalPurple,
    secondary = ElectricPurple,
    surface = Color.White,
    tertiary = SoftPurple,
    background = Color(0xFFF5F5F5),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onSurface = NightBlack,
    onBackground = NightBlack
)

@Composable
fun CozieTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    // Forcing a primarily dark theme look since the user requested Black and Purple
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
