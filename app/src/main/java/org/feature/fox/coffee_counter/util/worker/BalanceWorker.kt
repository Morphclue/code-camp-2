package org.feature.fox.coffee_counter.util.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.feature.fox.coffee_counter.BuildConfig
import org.feature.fox.coffee_counter.data.repository.UserRepository
import org.feature.fox.coffee_counter.di.services.AppPreference

@HiltWorker
class BalanceWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val userRepository: UserRepository,
    private val preference: AppPreference,
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        withContext(Dispatchers.IO) {
            val temp = userRepository.getTransactions(preference.getTag(BuildConfig.USER_ID))
            Log.d("WORK", "Worker running")
            Log.d("WORK", "Size of transactions is: ${temp.data?.size}")
        }
        return Result.success()
    }
}
