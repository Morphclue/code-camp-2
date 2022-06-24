package org.feature.fox.coffee_counter.ui.profile

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import org.feature.fox.coffee_counter.R
import org.feature.fox.coffee_counter.data.local.database.tables.User
import org.feature.fox.coffee_counter.ui.common.MoneyAppBar
import org.feature.fox.coffee_counter.ui.theme.CrayolaBrown

@Preview(showSystemUi = true)
@Composable
fun ProfileView() {
    val user = User(id = "a", name = "Max Mustermann", false, "123456789")
    Column(
        modifier = Modifier.padding(5.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MoneyAppBar(title = stringResource(R.string.profile_title))
        ProfileIcon()
        UserIdRow(user.id)
        UserNameRow(user.name)
        PasswordRow(user.password)
        RetypePasswordRow(user.password)
        if (user.isAdmin) AdminCheckbox(user.isAdmin)
        ButtonRow()
    }
}

@Composable
fun UserIdRow(userIdString: String) {
    var userId by rememberSaveable { mutableStateOf(userIdString) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.id_hint),
            textAlign = TextAlign.Center,
            modifier = Modifier.width(100.dp)
        )
        TextField(
            value = userId,
            onValueChange = { userId = it },
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
            ),
            shape = RoundedCornerShape(8.dp),
            trailingIcon = {
                IconButton(onClick = {}) {

                }
            } //FIXME: Center text exactly at the same position as password text fields
        )

    }
}

@Composable
fun UserNameRow(userNameString: String) {

    var userName by rememberSaveable { mutableStateOf(userNameString) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.name_hint),
            textAlign = TextAlign.Center,
            modifier = Modifier.width(100.dp)
        )
        TextField(
            value = userName,
            onValueChange = { userName = it },
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
            ),
            shape = RoundedCornerShape(8.dp),
            trailingIcon = {
                IconButton(onClick = {}) {
                }
            } //FIXME: Center text exactly at the same position as password text fields
        )

    }
}

@Composable
fun PasswordRow(passwordString: String) {

    var password by rememberSaveable { mutableStateOf(passwordString) }
    val showPassword = remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.password_hint),
            textAlign = TextAlign.Center,
            modifier = Modifier.width(100.dp)
        )
        TextField(
            value = password,
            onValueChange = { password = it },
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
            ),
            shape = RoundedCornerShape(8.dp),
            visualTransformation = if (showPassword.value) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                if (showPassword.value) {
                    IconButton(onClick = { showPassword.value = false }) {
                        Icon(
                            imageVector = Icons.Filled.Visibility,
                            contentDescription = stringResource(R.string.hide_password)
                        )
                    }
                } else {
                    IconButton(onClick = { showPassword.value = true }) {
                        Icon(
                            imageVector = Icons.Filled.VisibilityOff,
                            contentDescription = stringResource(R.string.show_password)
                        )
                    }
                }
            }
        )

    }
}

@Composable
fun RetypePasswordRow(passwordString: String) {
    var password by rememberSaveable { mutableStateOf(passwordString) }
    val showPassword = remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.re_enter_password_hint),
            textAlign = TextAlign.Center,
            modifier = Modifier.width(100.dp)
        )
        TextField(
            value = password,
            onValueChange = { password = it },
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
            ),
            shape = RoundedCornerShape(8.dp),
            visualTransformation = if (showPassword.value) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                if (showPassword.value) {
                    IconButton(onClick = { showPassword.value = false }) {
                        Icon(
                            imageVector = Icons.Filled.Visibility,
                            contentDescription = stringResource(R.string.hide_password)
                        )
                    }
                } else {
                    IconButton(onClick = { showPassword.value = true }) {
                        Icon(
                            imageVector = Icons.Filled.VisibilityOff,
                            contentDescription = stringResource(R.string.show_password)
                        )
                    }
                }
            }
        )

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
            contentDescription = null,
            modifier = Modifier
                .wrapContentSize()
                .size(150.dp),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun ButtonRow() {
    val notification = rememberSaveable() { mutableStateOf("") }
    if (notification.value.isNotEmpty()) {
        Toast.makeText(LocalContext.current, notification.value, Toast.LENGTH_SHORT).show()
        notification.value = ""
    }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Card(
                shape = CircleShape,
                backgroundColor = CrayolaBrown,
                modifier = Modifier
                    .padding(8.dp)
                    .size(50.dp)
            ) {
                IconButton(
                    onClick = { notification.value = "Profile updated" },
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
                    onClick = {
                        notification.value = "Profile deleted"
                    },
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
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
                    onClick = { notification.value = "Cancelled" },
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        null,
                        tint = Color.White,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

        }
    }
}
