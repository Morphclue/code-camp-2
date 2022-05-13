package org.feature.fox.coffee_counter.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.feature.fox.coffee_counter.R

@Preview(showSystemUi = true)
@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Title()
        LoginButton()
        SignUpButton()
    }
}

@Composable
fun Title() {
    Text(
        text = stringResource(id = R.string.app_name),
    )
}

@Composable
fun LoginButton() {
    Button(
        onClick = {},
        modifier = Modifier.fillMaxWidth(0.7f),
    ) {
        Text(
            text = stringResource(id = R.string.login),
        )
    }
}

@Composable
fun SignUpButton() {
    Button(
        onClick = {},
        modifier = Modifier.fillMaxWidth(0.7f),
    ) {
        Text(
            text = stringResource(id = R.string.sign_up),
        )
    }
}
