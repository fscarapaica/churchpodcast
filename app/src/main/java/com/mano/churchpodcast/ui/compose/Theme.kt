package com.mano.churchpodcast.ui.compose

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.mano.core_compose.BrightGreen
import androidx.compose.ui.graphics.Color
import com.mano.core_compose.DarkGray
import com.mano.core_compose.DarkGreen
import com.mano.core_compose.Dimensions
import com.mano.core_compose.LightGray
import com.mano.core_compose.LocalSpacing
import com.mano.core_compose.MediumGray
import com.mano.core_compose.Orange
import com.mano.core_compose.TextWhite

private val DarkColorPalette = darkColorScheme(
    primary = BrightGreen,
    inversePrimary = DarkGreen,
    secondary = Orange,
    background = MediumGray,
    onBackground = TextWhite,
    surface = LightGray,
    onSurface = TextWhite,
    onPrimary = Color.White,
    onSecondary = Color.White,
)

private val LightColorPalette = lightColorScheme(
    primary = BrightGreen,
    inversePrimary = DarkGreen,
    secondary = Orange,
    background = Color.White,
    onBackground = DarkGray,
    surface = Color.White,
    onSurface = DarkGray,
    onPrimary = Color.White,
    onSecondary = Color.White,
)

@Composable
fun ChurchPodcastTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }
    CompositionLocalProvider(LocalSpacing provides Dimensions()) {
        MaterialTheme(
            colorScheme = colors,
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }
}