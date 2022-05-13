package org.feature.fox.coffee_counter.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.feature.fox.coffee_counter.R
import org.feature.fox.coffee_counter.ui.theme.CrayolaBrown

@Preview(showSystemUi = true)
@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        SplashBox()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            LoginButton()
            SignUpButton()
        }
    }
}

@Composable
fun SplashBox() {
    Box(
        modifier = Modifier
            .size(
                LocalConfiguration.current.screenWidthDp.dp + 0.5.dp,
                LocalConfiguration.current.screenHeightDp.dp * 0.7f
            )
            .clip(RoundedCornerShape(0.dp, 0.dp, 30.dp, 30.dp))
            .background(CrayolaBrown)
    ) {
        BoxContent()
    }
}

@Composable
fun BoxContent() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Title()
        CoffeeImage()
    }
}

@Composable
fun CoffeeImage() {
    Text(
        text = "add image here",
    )
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
