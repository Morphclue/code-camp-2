package org.feature.fox.coffee_counter.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.feature.fox.coffee_counter.R

@Preview(showSystemUi = true)
@Composable
fun HomeScreen() {
    Column() {
        Title()
        LoginButton()
        SignUpButton()
    }
}

@Composable
fun Title() {
    Text(
        text = stringResource(id = R.string.app_name)
    )
}

@Composable
fun LoginButton() {
    Button(onClick = {}) {
        Text(
            text = stringResource(id = R.string.login)
        )
    }
}

@Composable
fun SignUpButton() {
    Button(onClick = {}) {
        Text(
            text = stringResource(id = R.string.sign_up)
        )
    }
}
