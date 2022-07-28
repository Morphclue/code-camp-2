package org.feature.fox.coffee_counter.di.services

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import org.feature.fox.coffee_counter.BuildConfig
import org.feature.fox.coffee_counter.data.local.database.tables.Achievement
import org.feature.fox.coffee_counter.data.local.database.tables.Item
import org.feature.fox.coffee_counter.data.local.database.tables.Purchase
import org.feature.fox.coffee_counter.data.repository.UserRepository
import org.feature.fox.coffee_counter.ui.common.showAchievementNotification
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AchievementGeneration @Inject constructor(
    @ApplicationContext context: Context, private val userRepository: UserRepository,
    private val preference: AppPreference,
) {

    suspend fun checkAchievements(
        items: List<Item>
    ) {
        val purchases: List<Purchase> =
            userRepository.getPurchaseListOfUserDb(preference.getTag(BuildConfig.USER_ID))
        val achievements: List<Achievement> =
            userRepository.getAchievementListOfUserDb(preference.getTag(BuildConfig.USER_ID))
        junkieAchievements(
            purchases,
            achievements,
            items,
        )

    }

    private suspend fun junkieAchievements(
        purchases: List<Purchase>,
        achievements: List<Achievement>,
        items: List<Item>,
    ) {
        items.forEach { item ->
            if (!achievements.any { it.name == "${item.name} Junkie" }) {
                var totalPurchases = 0
                purchases.filter { it.itemName == item.name }.forEach { purchase ->
                    totalPurchases += purchase.amount
                }
                if (totalPurchases >= 100) {
                    val junkieAchievement = Achievement(
                        name = "${item.name} Junkie",
                        userId = preference.getTag(BuildConfig.USER_ID),
                        timestamp = System.currentTimeMillis() / 1000,
                        description = "You are a ${item.name} Junkie, you drank over 100 ${item.name}",
                    )
                    // TODO maybe return as list and trigger outside this function
                    userRepository.insertAchievementDb(junkieAchievement)
                    showAchievementNotification(junkieAchievement)
                }
            }
        }
    }
}