package org.feature.fox.coffee_counter.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = CrayolaBrown,
    primaryVariant = LiverOrgan,
    secondary = CrayolaCopper
)

private val LightColorPalette = lightColors(
    primary = CrayolaBrown,
    primaryVariant = LiverOrgan,
    secondary = CrayolaCopper
)

@Composable
fun CoffeeCounterTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
