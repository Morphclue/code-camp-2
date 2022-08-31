package org.feature.fox.coffee_counter.ui.common

import android.content.Context
import android.widget.Toast
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import org.feature.fox.coffee_counter.util.IToast

/**
 * A [Toast] that is displayed on the screen.
 * @param viewModel The view model that is used to display the toast.
 * @param context The context of the application.
 */
@Composable
fun ToastMessage(viewModel: IToast, context: Context) {
    LaunchedEffect(rememberScaffoldState()) {
        viewModel.toast.collect { message ->
            Toast.makeText(
                context,
                message.asString(context),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
