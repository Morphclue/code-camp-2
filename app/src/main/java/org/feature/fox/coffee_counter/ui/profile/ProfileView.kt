package org.feature.fox.coffee_counter.ui.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.feature.fox.coffee_counter.R
import org.feature.fox.coffee_counter.ui.common.MoneyAppBar

@Preview(showSystemUi = true)
@Composable
fun ProfileView() {
    Column {
        MoneyAppBar(title = stringResource(R.string.profile_title))
    }
}
