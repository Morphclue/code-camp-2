package org.feature.fox.coffee_counter.ui.common

import android.app.Notification
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import org.feature.fox.coffee_counter.BaseApplication
import org.feature.fox.coffee_counter.BuildConfig
import org.feature.fox.coffee_counter.R
import org.feature.fox.coffee_counter.data.local.database.tables.Achievement

/**
 * Function to show a notification for an achievement.
 * @param achievement the achievement to show the notification for.
 */
fun showAchievementNotification(achievement: Achievement) {
    val notificationManager: NotificationManagerCompat =
        NotificationManagerCompat.from(BaseApplication.applicationContext)

    val notification: Notification = NotificationCompat.Builder(
        BaseApplication.applicationContext, BuildConfig.NOTIFICATION_CHANNEL_ID
    )
        .setSmallIcon(R.drawable.coffee)
        .setContentTitle(achievement.name)
        .setContentText(achievement.description)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
        .build()
    notificationManager.notify(1, notification)
}
