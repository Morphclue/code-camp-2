package org.feature.fox.coffee_counter.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class BalanceStateProvider : PreviewParameterProvider<Pair<String, MutableState<Double>>> {
    override val values: Sequence<Pair<String, MutableState<Double>>> =
        sequenceOf(Pair("Beverage", mutableStateOf(13.0)))
}

@Preview
@Composable
fun MoneyAppBar(
    @PreviewParameter(BalanceStateProvider::class) pair: Pair<String, MutableState<Double>>
) {
    TopAppBar(title = {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(text = pair.first)
            Spacer(modifier = Modifier.weight(1f))
            Text("${String.format("%.2f", pair.second.value)}€")
        }
    })
}
