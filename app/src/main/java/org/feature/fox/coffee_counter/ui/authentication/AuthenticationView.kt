package org.feature.fox.coffee_counter.ui.authentication

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.feature.fox.coffee_counter.R
import org.feature.fox.coffee_counter.ui.CoreActivity
import org.feature.fox.coffee_counter.ui.common.CommonTextField
import org.feature.fox.coffee_counter.ui.common.CustomButton
import org.feature.fox.coffee_counter.ui.common.PasswordTextField

@Preview(showSystemUi = true)
@Composable
fun AuthenticationViewPreview(
) {
    AuthenticationView(AuthenticationViewModelPreview())
}

@Composable
fun AuthenticationView(
    viewModel: IAuthenticationViewModel,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        LoginSignupHeader(viewModel)
        if (viewModel.loginState.value) LoginFragment(viewModel) else RegisterFragment(viewModel)
    }
}

@Composable
fun LoginFragment(viewModel: IAuthenticationViewModel) {
    val coroutineScope = rememberCoroutineScope()
    val showCoreActivity = viewModel.showCoreActivity.observeAsState()
    val context = LocalContext.current

    CommonTextField(state = viewModel.idState, label = stringResource(R.string.id_hint))
    PasswordTextField(
        state = viewModel.passwordState,
        label = stringResource(R.string.password_hint)
    )
    RememberMeCheckbox()
    CustomButton(
        onClick = {
            coroutineScope.launch {
                viewModel.login()
                if (showCoreActivity.value == true) {
                    context.startActivity(Intent(context, CoreActivity::class.java))
                }
            }
        },
        text = stringResource(R.string.login)
    )
}

@Composable
fun RegisterFragment(viewModel: IAuthenticationViewModel) {
    val coroutineScope = rememberCoroutineScope()

    CommonTextField(state = viewModel.nameState, label = stringResource(R.string.name_hint))
    CommonTextField(state = viewModel.idState, label = stringResource(R.string.optional_id_hint))
    PasswordTextField(
        state = viewModel.passwordState,
        label = stringResource(R.string.password_hint)
    )
    PasswordTextField(
        state = viewModel.reEnteredPasswordState,
        label = stringResource(R.string.re_enter_password_hint)
    )
    CustomButton(
        onClick = {
            coroutineScope.launch {
                viewModel.register()
            }
        },
        text = stringResource(R.string.sign_up)
    )
}

@Composable
fun LoginSignupHeader(viewModel: IAuthenticationViewModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        val dropShadow = Shadow(
            color = Color.LightGray,
            offset = Offset(8f, 8f),
            blurRadius = 8f
        )
        if (viewModel.loginState.value) {
            HeaderButton(stringResource(R.string.login), dropShadow)
            HeaderButton(
                text = stringResource(R.string.sign_up),
                onClick = { viewModel.loginState.value = false }
            )
        } else {
            HeaderButton(
                text = stringResource(R.string.login),
                onClick = { viewModel.loginState.value = true }
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
