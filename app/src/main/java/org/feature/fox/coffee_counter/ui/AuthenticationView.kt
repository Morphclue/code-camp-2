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
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.feature.fox.coffee_counter.R

class LoginStateProvider : PreviewParameterProvider<Boolean> {
    override val values: Sequence<Boolean> = sequenceOf(
        true,
        false,
    )
}

@Preview(showSystemUi = true)
@Composable
fun AuthenticationView(
    @PreviewParameter(LoginStateProvider::class) login: Boolean,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        val loginState = remember { mutableStateOf(login) }
        LoginSignupHeader(loginState)
        if (loginState.value) LoginFragment() else RegisterFragment()
    }
}

@Composable
fun LoginFragment() {
    NormalTextField(stringResource(R.string.id_hint))
    PasswordTextField(stringResource(R.string.password_hint))
    RememberMeCheckbox()
    CustomButton(
        text = stringResource(R.string.login)
    )
}

@Composable
fun RegisterFragment() {
    NormalTextField(stringResource(R.string.name_hint))
    NormalTextField(stringResource(R.string.optional_id_hint))
    PasswordTextField(stringResource(R.string.password_hint))
    PasswordTextField(stringResource(R.string.re_enter_password_hint))
    CustomButton(
        text = stringResource(R.string.sign_up)
    )
}

@Composable
fun LoginSignupHeader(loginState: MutableState<Boolean>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        val dropShadow = Shadow(
            color = Color.LightGray,
            offset = Offset(8f, 8f),
            blurRadius = 8f
        )
        if (loginState.value) {
            HeaderButton(stringResource(R.string.login), dropShadow)
            HeaderButton(
                text = stringResource(R.string.sign_up),
                onClick = { loginState.value = false }
            )
        } else {
            HeaderButton(
                text = stringResource(R.string.login),
                onClick = { loginState.value = true }
            )
            HeaderButton(stringResource(R.string.sign_up), dropShadow)
        }
    }
}

@Composable
fun HeaderButton(
    text: String,
    shadow: Shadow = Shadow(),
    onClick: (Int) -> Unit = {},
) {
    ClickableText(
        style = TextStyle(
            shadow = shadow,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
        ),
        text = AnnotatedString(text),
        onClick = onClick
    )
}

@Composable
fun NormalTextField(text: String) {
    val idState = remember { mutableStateOf(TextFieldValue()) }
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = idState.value,
        onValueChange = { idState.value = it },
        label = { Text(text = text) },
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        shape = RoundedCornerShape(8.dp)
    )
}

@Composable
fun PasswordTextField(text: String) {
    val passwordState = remember { mutableStateOf(TextFieldValue()) }
    val showPassword = remember { mutableStateOf(false) }
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = passwordState.value,
        onValueChange = { passwordState.value = it },
        label = { Text(text = text) },
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
