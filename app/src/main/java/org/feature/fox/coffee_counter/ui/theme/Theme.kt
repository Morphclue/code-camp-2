package org.feature.fox.coffee_counter.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val LightColorPalette = lightColors(
    primary = CrayolaBrown,
    primaryVariant = LiverOrgan,
    secondary = CrayolaCopper
)

@Composable
fun CoffeeCounterTheme(
    content: @Composable () -> Unit,
) {

    rememberSystemUiController().setSystemBarsColor(
        color = LiverOrgan
    )

    MaterialTheme(
        colors = LightColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
