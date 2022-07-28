package org.feature.fox.coffee_counter.ui.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch
import org.feature.fox.coffee_counter.R
import org.feature.fox.coffee_counter.data.local.database.tables.Achievement
import org.feature.fox.coffee_counter.ui.theme.LiverOrgan
import org.feature.fox.coffee_counter.ui.user.IUserListViewModel
import org.feature.fox.coffee_counter.ui.user.MoneyEditRow
import org.feature.fox.coffee_counter.ui.user.ProfilePicture
import org.feature.fox.coffee_counter.ui.user.ShareMoneyButton

@Composable
fun AchievementOverview(
    viewModel: IProfileViewModel,
) {
    if (!viewModel.achievementOverviewVisible.value) {
        return
    }
    Dialog(
        onDismissRequest = { viewModel.achievementOverviewVisible.value = false },
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colors.surface,
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(id = R.string.achievement_overview),
                    style = MaterialTheme.typography.subtitle1
                )
                viewModel.achievementList.forEach{ achievement ->
                    AchievementCard(viewModel, achievement)
                }
                AchievementOverviewDialogButtons(viewModel)
            }
        }
    }
}

@Composable
fun AchievementCard(viewModel: IProfileViewModel, achievement: Achievement){
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
                AchievementPicture(viewModel, achievement)

                Column {
                    Text(
                        achievement.name,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        achievement.description,
                        fontWeight = FontWeight.Light
                    )
                }
            }
        }
    }
}

@Composable
fun AchievementOverviewDialogButtons(viewModel: IProfileViewModel) {
    val scope = rememberCoroutineScope()
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        TextButton(onClick = {
            scope.launch {
                viewModel.achievementOverviewVisible.value = false
            }
        }) {
            Text(text = stringResource(id = R.string.ok))
        }
    }
}

@Composable
fun AchievementPicture(viewModel: IProfileViewModel, achievement: Achievement) {
    val painter = painterResource(achievement.icon)

    Image(
        painter = painter,
        contentDescription = stringResource(R.string.achievement_image_label),
        modifier = Modifier
            .wrapContentSize()
            .size(30.dp)
            .clip(CircleShape)
            .border(1.dp, LiverOrgan, CircleShape),
        contentScale = ContentScale.Crop,
    )
}
