package org.feature.fox.coffee_counter.di.services


import org.feature.fox.coffee_counter.BuildConfig
import org.feature.fox.coffee_counter.R
import org.feature.fox.coffee_counter.data.local.database.tables.Achievement
import org.feature.fox.coffee_counter.data.local.database.tables.Item
import org.feature.fox.coffee_counter.data.local.database.tables.Purchase
import org.feature.fox.coffee_counter.data.repository.UserRepository
import org.feature.fox.coffee_counter.ui.common.showAchievementNotification
import org.feature.fox.coffee_counter.util.UIText
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AchievementGeneration @Inject constructor(
    private val userRepository: UserRepository,
    private val preference: AppPreference,
) {

    suspend fun checkAchievements(
        items: List<Item>
    ) {
        val purchases: List<Purchase> =
            userRepository.getPurchaseListOfUserDb(preference.getTag(BuildConfig.USER_ID))
        val achievements: List<Achievement> =
            userRepository.getAchievementListOfUserDb(preference.getTag(BuildConfig.USER_ID))
        junkieAchievements(purchases, achievements, items)
        nightPurchaseAchievement(purchases, achievements)
        moneySpenderAchievement(purchases, achievements)
    }

    private suspend fun moneySpenderAchievement(
        purchases: List<Purchase>,
        achievements: List<Achievement>
    ) {
        val achievement1Name = UIText.StringResource(R.string.rookie_numbers).toString()
        val achievement1Description =
            UIText.StringResource(R.string.rookie_numbers_description).toString()

        if (!achievements.any { it.name == achievement1Name }) {
            var totalSpentMoney = 0.0
            purchases.forEach { purchase ->
                totalSpentMoney += purchase.totalValue
            }
            if (totalSpentMoney <= -100) {
                val cashAchievement = Achievement(
                    name = achievement1Name,
                    userId = preference.getTag(BuildConfig.USER_ID),
                    timestamp = System.currentTimeMillis() / 1000,
                    description = achievement1Description,
                    icon = R.drawable.rookie_numbers
                )
                userRepository.insertAchievementDb(cashAchievement)
                showAchievementNotification(cashAchievement)
            }
        }

        val achievement2Name = UIText.StringResource(R.string.rich_bitch).toString()
        val achievement2Description =
            UIText.StringResource(R.string.rich_bitch_description).toString()

        if (!achievements.any { it.name == achievement2Name }) {
            var totalSpentMoney = 0.0
            purchases.forEach { purchase ->
                totalSpentMoney += purchase.totalValue
            }
            if (totalSpentMoney <= -1000) {
                val cashAchievement = Achievement(
                    name = achievement2Name,
                    userId = preference.getTag(BuildConfig.USER_ID),
                    timestamp = System.currentTimeMillis() / 1000,
                    description = achievement2Description,
                    icon = R.drawable.rich_bitch
                )
                userRepository.insertAchievementDb(cashAchievement)
                showAchievementNotification(cashAchievement)
            }
        }

        val achievement3Name = UIText.StringResource(R.string.the_one_percent).toString()
        val achievement3Description =
            UIText.StringResource(R.string.the_one_percent_description).toString()

        if (!achievements.any { it.name == achievement3Name }) {
            var totalSpentMoney = 0.0
            purchases.forEach { purchase ->
                totalSpentMoney += purchase.totalValue
            }
            if (totalSpentMoney <= -10000) {
                val cashAchievement = Achievement(
                    name = achievement3Name,
                    userId = preference.getTag(BuildConfig.USER_ID),
                    timestamp = System.currentTimeMillis() / 1000,
                    description = achievement3Description,
                    icon = R.drawable.the_one_percent
                )
                userRepository.insertAchievementDb(cashAchievement)
                showAchievementNotification(cashAchievement)
            }
        }
    }

    private suspend fun junkieAchievements(
        purchases: List<Purchase>,
        achievements: List<Achievement>,
        items: List<Item>,
    ) {
        items.forEach { item ->
            val achievementName = "${item.name} ${UIText.StringResource(R.string.junkie)}"
            val achievementDescription =
                "${UIText.StringResource(R.string.drank_over_100)} ${item.name}"
            if (!achievements.any { it.name == achievementName }) {
                var totalPurchases = 0
                purchases.filter { it.itemName == item.name }.forEach { purchase ->
                    totalPurchases += purchase.amount
                }
                if (totalPurchases >= 100) {
                    val junkieAchievement = Achievement(
                        name = achievementName,
                        userId = preference.getTag(BuildConfig.USER_ID),
                        timestamp = System.currentTimeMillis() / 1000,
                        description = achievementDescription,
                        icon = R.drawable.coffee
                    )
                    userRepository.insertAchievementDb(junkieAchievement)
                    showAchievementNotification(junkieAchievement)
                }
            }
        }
    }

    private suspend fun nightPurchaseAchievement(
        purchases: List<Purchase>,
        achievements: List<Achievement>,
    ) {
        val achievementName = UIText.StringResource(R.string.weak_sleep).toString()
        val achievementDescription =
            UIText.StringResource(R.string.weak_sleep_description).toString()
        val purchaseTime: LocalTime = LocalTime.parse(
            SimpleDateFormat(
                "HH:mm:ss",
                Locale.GERMAN
            ).format(purchases.last().timestamp)
        )
        if (!achievements.any { it.name == achievementName } &&
            (purchaseTime.isAfter(LocalTime.parse("00:00:00")) &&
                    purchaseTime.isBefore(LocalTime.parse("04:00:00")))
        ) {
            val nightAchievement = Achievement(
                name = achievementName,
                userId = preference.getTag(BuildConfig.USER_ID),
                timestamp = System.currentTimeMillis() / 1000,
                description = achievementDescription,
                icon = R.drawable.weak_sleep
            )
            userRepository.insertAchievementDb(nightAchievement)
            showAchievementNotification(nightAchievement)
        }
    }
}
