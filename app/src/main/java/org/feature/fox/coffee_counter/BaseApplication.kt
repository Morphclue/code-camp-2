package org.feature.fox.coffee_counter

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication : Application() {

    init {
        instance = this
    }

    companion object {
        lateinit var instance: BaseApplication
            private set

        val applicationContext: Context
            get() {
                return instance.applicationContext
            }
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        val notificationChannel = NotificationChannel(
            BuildConfig.NOTIFICATION_CHANNEL_ID,
            "Notification Channel",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationChannel.description = "This is the notification Channel"
        val manager = getSystemService(
            NotificationManager::class.java
        )
        manager.createNotificationChannel(notificationChannel)
    }
}
