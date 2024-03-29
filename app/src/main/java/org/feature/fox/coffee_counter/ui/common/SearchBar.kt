package org.feature.fox.coffee_counter.ui.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import org.feature.fox.coffee_counter.R
import org.feature.fox.coffee_counter.ui.theme.CrayolaCopper

/**
 * A search bar that can be used to search for something.
 * @param modifier Modifier to apply to the search bar.
 * @param fraction The fraction of the search bar.
 * @param state The state of the search bar.
 * @param onValueChanged The callback for when the value of the search bar changes.
 */
@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    fraction: Float = 1.0f,
    state: MutableState<TextFieldValue>,
    onValueChanged: () -> Unit = {},
) {
    TextField(
        value = state.value,
        onValueChange = {
            state.value = it
            onValueChanged()
        },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White,
            focusedIndicatorColor = CrayolaCopper,
            unfocusedIndicatorColor = CrayolaCopper,
        ),
        placeholder = {
            Text(stringResource(R.string.search_hint))
        },
        modifier = modifier
            .fillMaxWidth(fraction)
            .heightIn(min = 56.dp)
            .padding(all = 5.dp),
        shape = RoundedCornerShape(50.dp),
    )
}
