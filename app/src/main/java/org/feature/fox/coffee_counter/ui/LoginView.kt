package org.feature.fox.coffee_counter.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.feature.fox.coffee_counter.R
import org.feature.fox.coffee_counter.ui.theme.CoffeeCounterTheme

@Preview(showSystemUi = true)
@Composable
fun LoginView() {
    CoffeeCounterTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            LoginSignupHeader()
            IdTextField()
            PasswordTextField()
        }
    }
}

@Composable
fun LoginSignupHeader() {
    Text(
        text = "todo"
    )
}

@Composable
fun IdTextField() {
    val idState = remember { mutableStateOf(TextFieldValue()) }
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = idState.value,
        onValueChange = { idState.value = it },
        label = { Text(text = stringResource(R.string.id_hint)) },
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        shape = RoundedCornerShape(8.dp),
        visualTransformation = PasswordVisualTransformation()
    )
}

@Composable
fun PasswordTextField() {
    val passwordState = remember { mutableStateOf(TextFieldValue()) }
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = passwordState.value,
        onValueChange = { passwordState.value = it },
        label = { Text(text = stringResource(R.string.password_hint)) },
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        shape = RoundedCornerShape(8.dp),
        visualTransformation = PasswordVisualTransformation()
    )
}
