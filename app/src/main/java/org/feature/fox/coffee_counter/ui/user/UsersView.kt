package org.feature.fox.coffee_counter.ui.user

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch
import org.feature.fox.coffee_counter.R
import org.feature.fox.coffee_counter.data.models.response.UserIdResponse
import org.feature.fox.coffee_counter.ui.common.LoadingAnimation
import org.feature.fox.coffee_counter.ui.common.MoneyAppBar
import org.feature.fox.coffee_counter.ui.common.SearchBar
import org.feature.fox.coffee_counter.ui.common.ToastMessage
import org.feature.fox.coffee_counter.ui.items.LoadingBox
import org.feature.fox.coffee_counter.ui.theme.LiverOrgan

@Preview(showSystemUi = true)
@Composable
fun UsersViewPreview() {
    val preview = UserListViewModelPreview()
    preview.userList.add(UserIdResponse("a", "Julian", 15555555.0))
    preview.userList.add(UserIdResponse("b", "Kevin", -15.0))
    preview.userList.add(UserIdResponse("c", "Steffen", 42.0))
    UsersView(preview)
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun UsersView(viewModel: IUserListViewModel) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    ToastMessage(viewModel, context)
    FundingDialog(viewModel)
    AddUserDialog(viewModel)
    SendMoneyDialog(viewModel)

    Scaffold(
        topBar = { MoneyAppBar(Pair(stringResource(R.string.user_list_title), viewModel.balance)) },
    ) {
        Column {
            coroutineScope.launch {
                viewModel.getTotalBalance()
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            )
            {
                SearchBar(fraction = 0.7f)
                Button(
                    shape = CircleShape,
                    onClick = {
                        viewModel.editDialogVisible.value = true
                    })
                {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Edit",
                    )
                }
            }
            if (viewModel.isLoaded.value) UserList(viewModel) else LoadingBox()
        }
    }
}

@Composable
fun LoadingBox() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LoadingAnimation()
    }
}

@Composable
fun UserList(viewModel: IUserListViewModel) {
    Column {
        Column(
            modifier = Modifier
                .verticalScroll(viewModel.scrollState)
                .padding(5.dp)
                .fillMaxWidth()
                .weight(1f)
                .height((IntrinsicSize.Min)),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            viewModel.userList.forEach { user ->
                UserRow(viewModel, user)
            }
            Box(Modifier.height(50.dp))
        }
    }
}

@Composable
fun UserRow(
    viewModel: IUserListViewModel,
    user: UserIdResponse,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(5.dp),
        elevation = 5.dp,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp),
            ) {
                ProfilePicture(viewModel, user.id)
                Text(
                    user.name,
                    fontWeight = FontWeight.Medium
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                    if (viewModel.isAdminState.value) MoneyEditRow(viewModel, user)
                    ShareMoneyButton(viewModel, user)
                }
            }
        }
    }
}

@Composable
fun ProfilePicture(viewModel: IUserListViewModel, id: String) {
    val painter = if (viewModel.userIdPictureMap[id] == null) {
        rememberAsyncImagePainter(R.drawable.ic_baseline_person_24)
    } else {
        rememberAsyncImagePainter(viewModel.userIdPictureMap[id])
    }
    Image(
        painter = painter,
        contentDescription = stringResource(R.string.profile_image_label),
        modifier = Modifier
            .wrapContentSize()
            .size(30.dp)
            .clip(CircleShape)
            .border(1.dp, LiverOrgan, CircleShape),
        contentScale = ContentScale.Crop,
    )
}

@Composable
fun ShareMoneyButton(viewModel: IUserListViewModel, user: UserIdResponse) {
    Button(
        modifier = Modifier
            .size(30.dp),
        contentPadding = PaddingValues(0.dp),
        onClick = {
            viewModel.currentUser.value = user
            viewModel.sendMoneyDialogVisible.value = true
        },
    ) {
        Icon(
            imageVector = Icons.Filled.Share,
            contentDescription = "Share",
        )
    }
}

@Composable
fun MoneyEditRow(
    viewModel: IUserListViewModel,
    user: UserIdResponse,
) {
    Text(
        "${String.format("%.2f", user.balance)}€",
        fontWeight = FontWeight.Medium,
        color = Color.Gray,
        modifier = Modifier
            .width(110.dp)
            .padding(5.dp),
        textAlign = TextAlign.End
    )
    Button(
        modifier = Modifier
            .size(30.dp),
        contentPadding = PaddingValues(0.dp),
        onClick = {
            viewModel.currentUser.value = user
            viewModel.fundingDialogVisible.value = true
        })
    {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = "Add",
        )
    }
}
