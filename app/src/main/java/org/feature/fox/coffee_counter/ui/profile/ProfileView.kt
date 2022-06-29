package org.feature.fox.coffee_counter.ui.profile

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.SaveAlt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import org.feature.fox.coffee_counter.R
import org.feature.fox.coffee_counter.data.local.database.tables.User
import org.feature.fox.coffee_counter.ui.common.CommonTextField
import org.feature.fox.coffee_counter.ui.common.MoneyAppBar
import org.feature.fox.coffee_counter.ui.common.PasswordTextField
import org.feature.fox.coffee_counter.ui.theme.CrayolaBrown


@SuppressLint("UnrememberedMutableState")
@Preview(showSystemUi = true)
@Composable
fun ProfileView() {
    val user = User(id = "a", name = "Max Mustermann", true, "123456789")
    val nameState = mutableStateOf(TextFieldValue(user.name))
    val idState = mutableStateOf(TextFieldValue(user.id))
    val passwordState = mutableStateOf(TextFieldValue(user.password))
    val retypePasswordState = mutableStateOf(TextFieldValue(user.password))

    BoxWithConstraints {
        Column {
            MoneyAppBar(title = stringResource(R.string.profile_title))
            Column(
                modifier = Modifier
                    .padding(5.dp)
                    .verticalScroll(rememberScrollState())
                    .height(this@BoxWithConstraints.maxHeight),
                verticalArrangement = Arrangement.spacedBy(16.dp),
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
                ButtonRow()
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
        Checkbox(
            checked = isChecked.value,
            onCheckedChange = { isChecked.value = it }
        )
        Text(text = stringResource(R.string.admin_label))
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
fun ButtonRow() {

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            Card(
                shape = CircleShape,
                backgroundColor = CrayolaBrown,
                modifier = Modifier
                    .padding(8.dp)
                    .size(50.dp)
            ) {
                IconButton(
                    onClick = {},
                ) {
                    Icon(
                        imageVector = Icons.Filled.SaveAlt,
                        null,
                        tint = Color.White,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Card(
                shape = CircleShape,
                backgroundColor = CrayolaBrown,
                modifier = Modifier
                    .padding(8.dp)
                    .size(50.dp)
            ) {
                IconButton(
                    onClick = {},
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        null,
                        tint = Color.White,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
