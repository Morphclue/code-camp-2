package org.feature.fox.coffee_counter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.AndroidEntryPoint
import org.feature.fox.coffee_counter.ui.authentication.HomeView
import org.feature.fox.coffee_counter.ui.theme.CoffeeCounterTheme
import org.feature.fox.coffee_counter.util.worker.BalanceWorker
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val constraints =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val request = PeriodicWorkRequestBuilder<BalanceWorker>(5, TimeUnit.SECONDS).setConstraints(
            constraints
        ).build()
        WorkManager.getInstance(this).enqueue(request)
        setContent {
            CoffeeCounterTheme {
                HomeView()
            }
        }
    }
}
