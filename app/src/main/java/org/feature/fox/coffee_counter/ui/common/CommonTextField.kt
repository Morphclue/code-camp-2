package org.feature.fox.coffee_counter.ui.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import org.feature.fox.coffee_counter.ui.theme.CrayolaBrown

@Composable
fun CommonTextField(
    state: MutableState<TextFieldValue>,
    label: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    val textState by remember { mutableStateOf(state) }
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = textState.value,
        onValueChange = { textState.value = it },
        label = { Text(label) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = CrayolaBrown,
            unfocusedBorderColor = Color.LightGray
        ),
        keyboardOptions = keyboardOptions,
        shape = RoundedCornerShape(8.dp)
    )
}
