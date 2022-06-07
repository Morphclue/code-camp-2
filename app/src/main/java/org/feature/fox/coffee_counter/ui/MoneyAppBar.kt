package org.feature.fox.coffee_counter.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class TitleStateProvider : PreviewParameterProvider<String> {
    override val values: Sequence<String> = sequenceOf(
        "Beverage",
        "History",
    )
}

@Preview()
@Composable
fun MoneyAppBar(
    @PreviewParameter(TitleStateProvider::class) title: String,
) {
    TopAppBar(title = {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(text = title)
            Spacer(modifier = Modifier.weight(1f))
            Text("13.37€")
        }
    })
}
