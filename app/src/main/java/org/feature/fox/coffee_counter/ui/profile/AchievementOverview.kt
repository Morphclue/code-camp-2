package org.feature.fox.coffee_counter.ui.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.launch
import org.feature.fox.coffee_counter.R

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
//                viewModel.achievementList.forEach{ achievement ->
//                    AchievementCard(viewModel, achievement)
//                }
                AchievementOverviewDialogButtons(viewModel)
            }
        }
    }
}

//@Composable
//fun AchievementCard(viewModel: IProfileViewModel, achievement: Achievement){
//    val painter = rememberAsyncImagePainter(achievement.icon)
//
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(5.dp),
//        elevation = 5.dp
//    ) {
//        Column {
//            Text(item.name, fontWeight = FontWeight.Medium)
//            Text("${String.format("%.2f", item.price)}€", color = Color.Gray)
//        }
//
//        Column {
//            Row(
//                horizontalArrangement = Arrangement.spacedBy(10.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Text(
//                    "${viewModel.itemsInShoppingCartState.first { it.id == item.id }.amount}" +
//                            "/${item.amount}",
//                    fontWeight = FontWeight.Medium,
//                    modifier = Modifier.width(70.dp)
//                )
//
//                AddToCartButton(viewModel, item)
//                RemoveFromCartButton(viewModel, item)
//            }
//        }
//    }
//    }
//}

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