package org.feature.fox.coffee_counter.util.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.feature.fox.coffee_counter.data.repository.UserRepository
import org.feature.fox.coffee_counter.di.services.AppPreference

@HiltWorker
class BalanceWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
) : CoroutineWorker(context, workerParams) {

    // TODO fetch API and put values into DB. Implement queries that return live data from db
    // and observe them inside the viewModels to update balance as background task
    override suspend fun doWork(): Result {
        withContext(Dispatchers.IO) {
            //val temp = userRepository.getTransactions(preference.getTag(BuildConfig.USER_ID))
        }
        return Result.success()
    }
}
