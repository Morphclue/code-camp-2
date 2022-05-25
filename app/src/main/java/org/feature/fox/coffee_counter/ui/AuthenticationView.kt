package org.feature.fox.coffee_counter.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.feature.fox.coffee_counter.R

@Preview(showSystemUi = true)
@Composable
fun AuthenticationView() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        LoginSignupHeader()
        LoginFragment()
    }
}

@Composable
fun LoginFragment() {
    IdTextField()
    PasswordTextField()
    RememberMeCheckbox()
    CustomButton(
        text = stringResource(R.string.login)
    )
}

@Composable
fun LoginSignupHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        val dropShadow = Shadow(
            color = Color.LightGray,
            offset = Offset(8f, 8f),
            blurRadius = 8f
        )
        HeaderButton(stringResource(R.string.login), dropShadow)
        HeaderButton(stringResource(R.string.sign_up))
    }
}

@Composable
fun HeaderButton(text: String, shadow: Shadow = Shadow()) {
    ClickableText(
        style = TextStyle(
            shadow = shadow,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
        ),
        text = AnnotatedString(text),
        onClick = {}
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
    val showPassword = remember { mutableStateOf(false) }
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

@Composable
fun RememberMeCheckbox() {
    val isChecked = remember { mutableStateOf(false) }
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isChecked.value,
            onCheckedChange = { isChecked.value = it }
        )
        Text(text = stringResource(R.string.remember_me))
    }
}
