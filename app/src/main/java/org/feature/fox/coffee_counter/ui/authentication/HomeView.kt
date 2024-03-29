package org.feature.fox.coffee_counter.ui.authentication

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.feature.fox.coffee_counter.R
import org.feature.fox.coffee_counter.ui.common.CustomButton
import org.feature.fox.coffee_counter.ui.theme.CrayolaBrown

/**
 * Preview for the HomeView.
 */
@Preview(showSystemUi = true)
@Composable
fun HomeView() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        SplashBox()
        ButtonBox()
    }
}

/**
 * SplashBox contains the BoxContent for the SplashScreen.
 */
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

/**
 * BoxContent contains the Title and the Image for the SplashScreen.
 */
@Composable
fun BoxContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(50.dp)
    ) {
        Title()
        CoffeeImage()
    }
}

/**
 * Holds the image of the coffee.
 */
@Composable
fun CoffeeImage() {
    Image(painterResource(id = R.drawable.coffee), "Image of Coffee")
}

/**
 * Holds the title of the SplashScreen.
 */
@Composable
fun Title() {
    Text(
        text = stringResource(id = R.string.app_name),
        modifier = Modifier.padding(top = 100.dp),
        fontSize = 30.sp
    )
}

/**
 * Holds the Login and Sign Up button.
 * Depending on the button clicked the [AuthenticationActivity] with corresponding Intents is started.
 */
@Composable
fun ButtonBox() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        val context = LocalContext.current

        CustomButton(
            text = stringResource(id = R.string.login),
            fraction = 0.7f,
            onClick = {
                context.startActivity(Intent(context,
                    AuthenticationActivity::class.java).putExtra("login", true))
            }
        )
        CustomButton(
            stringResource(id = R.string.sign_up),
            fraction = 0.7f,
            onClick = {
                context.startActivity(Intent(context,
                    AuthenticationActivity::class.java).putExtra("login", false))
            }
        )
    }
}
