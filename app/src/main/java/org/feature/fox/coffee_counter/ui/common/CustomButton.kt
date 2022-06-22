package org.feature.fox.coffee_counter.ui.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CustomButton(text: String, fraction: Float = 1f, onClick: () -> Unit = {}) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(fraction = fraction),
    ) {
        Text(
            text = text,
        )
    }
}
