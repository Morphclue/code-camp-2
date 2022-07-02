package org.feature.fox.coffee_counter.ui.profile

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import org.feature.fox.coffee_counter.BuildConfig
import org.feature.fox.coffee_counter.R
import org.feature.fox.coffee_counter.data.local.database.tables.User
import org.feature.fox.coffee_counter.ui.common.CommonTextField
import org.feature.fox.coffee_counter.ui.common.CustomButton
import org.feature.fox.coffee_counter.ui.common.MoneyAppBar
import org.feature.fox.coffee_counter.ui.common.PasswordTextField

@SuppressLint("UnrememberedMutableState")
@Preview(showSystemUi = true)
@Composable
fun ProfileView() {
    val user = User(id = "a", name = "Max Mustermann", true, "123456789")
    val nameState = mutableStateOf(TextFieldValue(user.name))
    val idState = mutableStateOf(TextFieldValue(user.id))
    val passwordState = mutableStateOf(TextFieldValue(user.password))
    val retypePasswordState = mutableStateOf(TextFieldValue(user.password))
    val additionalScrollDp = 120.dp

    BoxWithConstraints {
        Column {
            MoneyAppBar(title = stringResource(R.string.profile_title))
            Column(
                modifier = Modifier
                    .padding(4.dp)
                    .verticalScroll(rememberScrollState())
                    .height(this@BoxWithConstraints.maxHeight + additionalScrollDp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ProfileIcon()
                CommonTextField(state = idState, label = stringResource(id = R.string.id_hint))
                CommonTextField(state = nameState, label = stringResource(id = R.string.name_hint))
                PasswordTextField(
                    state = passwordState,
                    label = stringResource(id = R.string.password_hint)
                )
                PasswordTextField(
                    state = retypePasswordState,
                    label = stringResource(id = R.string.re_enter_password_hint)
                )
                if (user.isAdmin) AdminCheckbox(user.isAdmin)
                ProfileButtons()
            }
        }
    }
}

@Composable
fun AdminCheckbox(isAdmin: Boolean) {
    val isChecked = remember { mutableStateOf(isAdmin) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(text = stringResource(R.string.admin_label))
        Switch(
            checked = isChecked.value,
            onCheckedChange = { isChecked.value = it }
        )
    }
}


@Composable
fun ProfileIcon() {
    val painter = rememberAsyncImagePainter(R.drawable.ic_baseline_person_24)

    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painter,
            contentDescription = stringResource(R.string.profile_image_label),
            modifier = Modifier
                .wrapContentSize()
                .size(150.dp),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun ProfileButtons() {
    CustomButton(text = stringResource(R.string.update_profile), fraction = 0.9f)
    CustomButton(text = stringResource(R.string.logout), fraction = 0.9f)

    val versionName = BuildConfig.VERSION_NAME
    Text(text = "version $versionName", fontWeight = FontWeight.Light)

    Button(
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
        onClick = {},
        modifier = Modifier.fillMaxWidth(0.9f),
    ) {
        Text(
            text = stringResource(R.string.delete_account),
            color = Color.Red
        )
    }
}
