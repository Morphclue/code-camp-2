package org.feature.fox.coffee_counter.ui.profile


import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.launch
import org.feature.fox.coffee_counter.R
import org.feature.fox.coffee_counter.data.local.database.tables.Achievement
import org.feature.fox.coffee_counter.ui.theme.LiverOrgan


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
            Column {
                Text(
                    text = stringResource(id = R.string.achievement_overview),
                    style = MaterialTheme.typography.subtitle1,
                )
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .verticalScroll(ScrollState(0))
                ) {
                    viewModel.achievementList.forEach { achievement ->
                        AchievementCard(achievement)
                    }
                    //FIXME move outside of this column but without pushing it out of the Dialog
                    AchievementOverviewDialogButtons(viewModel)
                }
            }
        }
    }
}

@Composable
fun AchievementCard(achievement: Achievement) {
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
                AchievementPicture(achievement)

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
fun AchievementPicture(achievement: Achievement) {
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
