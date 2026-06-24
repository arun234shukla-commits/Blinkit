package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = PremiumGreenPrimary,
    secondary = PremiumGoldSecondary,
    tertiary = AccentSuccess,
    background = Slate900Background,
    surface = Slate800Surface,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = TextLightPrimary,
    onSurface = TextLightPrimary,
    surfaceVariant = Color(0xFF374151),
    onSurfaceVariant = TextLightSecondary
)

private val LightColorScheme = lightColorScheme(
    primary = PremiumGreenPrimary,
    secondary = PremiumGoldSecondary,
    tertiary = AccentSuccess,
    background = LightBackground,
    surface = LightSurface,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = TextDarkPrimary,
    onSurface = TextDarkPrimary,
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = TextDarkSecondary
)

@Composable
fun QuickBasketTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

// Retro-compatibility name if referenced elsewhere
@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    QuickBasketTheme(darkTheme = darkTheme, content = content)
}
