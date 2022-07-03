package org.feature.fox.coffee_counter.ui.profile

import android.content.Context
import android.content.Intent
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.SaveAlt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch
import org.feature.fox.coffee_counter.MainActivity
import org.feature.fox.coffee_counter.R
import org.feature.fox.coffee_counter.ui.common.CommonTextField
import org.feature.fox.coffee_counter.ui.common.MoneyAppBar
import org.feature.fox.coffee_counter.ui.common.PasswordTextField
import org.feature.fox.coffee_counter.ui.common.ToastMessage
import org.feature.fox.coffee_counter.ui.theme.CrayolaBrown

@Preview(showSystemUi = true)
@Composable
fun ProfileViewPreview() {
    ProfileView(ProfileViewModelPreview())
}

@Composable
fun ProfileView(
    viewModel: IProfileViewModel,
) {
    val context = LocalContext.current
    ToastMessage(viewModel, context)

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
                CommonTextField(viewModel.idState, label = stringResource(id = R.string.id_hint))
                CommonTextField(viewModel.nameState,
                    label = stringResource(id = R.string.name_hint))
                PasswordTextField(
                    state = viewModel.passwordState,
                    label = stringResource(id = R.string.password_hint)
                )
                PasswordTextField(
                    state = viewModel.retypePasswordState,
                    label = stringResource(id = R.string.re_enter_password_hint)
                )
                if (viewModel.isAdminState.value) AdminCheckbox(viewModel)
                ButtonRow(viewModel, context)
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
        Checkbox(
            checked = viewModel.isAdminState.value,
            onCheckedChange = { viewModel.isAdminState.value = it }
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
fun ButtonRow(viewModel: IProfileViewModel, context: Context) {
    val coroutineScope = rememberCoroutineScope()
    val showMainActivity = viewModel.showMainActivity.observeAsState()

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
                    onClick = {
                        coroutineScope.launch {
                            viewModel.updateUser()
                        }
                    },
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
                        coroutineScope.launch {
                            viewModel.deleteUser()
                            if (showMainActivity.value == true) {
                                context.startActivity(Intent(context, MainActivity::class.java))
                            }
                        }
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
        }
    }
}
