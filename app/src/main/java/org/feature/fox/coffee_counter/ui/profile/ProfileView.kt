package org.feature.fox.coffee_counter.ui.profile

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch
import org.feature.fox.coffee_counter.BuildConfig
import org.feature.fox.coffee_counter.MainActivity
import org.feature.fox.coffee_counter.R
import org.feature.fox.coffee_counter.ui.common.CommonTextField
import org.feature.fox.coffee_counter.ui.common.CustomButton
import org.feature.fox.coffee_counter.ui.common.MoneyAppBar
import org.feature.fox.coffee_counter.ui.common.PasswordTextField
import org.feature.fox.coffee_counter.ui.common.ToastMessage

@Preview(showSystemUi = true)
@Composable
fun ProfileViewPreview() {
    ProfileView(ProfileViewModelPreview())
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ProfileView(
    viewModel: IProfileViewModel,
) {
    val context = LocalContext.current
    ToastMessage(viewModel, context)
    val additionalScrollDp = 120.dp
    val coroutineScope = rememberCoroutineScope()

    BoxWithConstraints {
        Column {
            MoneyAppBar(Pair(stringResource(R.string.history_title), viewModel.balance))
            Column(
                modifier = Modifier
                    .padding(4.dp)
                    .verticalScroll(rememberScrollState())
                    .height(this@BoxWithConstraints.maxHeight + additionalScrollDp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                coroutineScope.launch {
                    viewModel.getTotalBalance()
                }
                ProfileIcon()
                CommonTextField(viewModel.idState, label = stringResource(id = R.string.id_hint))
                CommonTextField(
                    viewModel.nameState,
                    label = stringResource(id = R.string.name_hint)
                )
                PasswordTextField(
                    state = viewModel.passwordState,
                    label = stringResource(id = R.string.password_hint)
                )
                PasswordTextField(
                    state = viewModel.retypePasswordState,
                    label = stringResource(id = R.string.re_enter_password_hint)
                )
                if (viewModel.isAdminState.value) AdminCheckbox(viewModel)
                ProfileButtons(viewModel, context)
            }
        }
    }
}

@Composable
fun AdminCheckbox(viewModel: IProfileViewModel) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(text = stringResource(R.string.admin_label))
        Switch(
            checked = viewModel.isAdminState.value,
            onCheckedChange = { viewModel.isAdminState.value = it }
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
                .size(150.dp)
                .clickable(
                    enabled = true,
                    onClickLabel = "Clickable profile image",
                    onClick = {

                    }
                ),
            contentScale = ContentScale.Crop,
        )
    }
}

@Composable
fun ProfileButtons(viewModel: IProfileViewModel, context: Context) {
    val coroutineScope = rememberCoroutineScope()
    val showMainActivity = viewModel.showMainActivity.observeAsState()

    CustomButton(text = stringResource(R.string.update_profile), fraction = 0.9f, onClick = {
        coroutineScope.launch {
            viewModel.updateUser()
        }
    })
    CustomButton(text = stringResource(R.string.logout), fraction = 0.9f)

    val versionName = BuildConfig.VERSION_NAME
    Text(text = "version $versionName", fontWeight = FontWeight.Light)

    Button(
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
        onClick = {
            coroutineScope.launch {
                viewModel.deleteUser()
                if (showMainActivity.value == true) {
                    context.startActivity(Intent(context, MainActivity::class.java))
                }
            }
        },
        modifier = Modifier.fillMaxWidth(0.9f),
    ) {
        Text(
            text = stringResource(R.string.delete_account),
            color = Color.Red
        )
    }
}
