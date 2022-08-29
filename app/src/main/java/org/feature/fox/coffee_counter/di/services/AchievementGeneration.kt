package org.feature.fox.coffee_counter.di.services


import org.feature.fox.coffee_counter.BuildConfig
import org.feature.fox.coffee_counter.R
import org.feature.fox.coffee_counter.data.local.database.tables.Achievement
import org.feature.fox.coffee_counter.data.local.database.tables.Item
import org.feature.fox.coffee_counter.data.local.database.tables.Purchase
import org.feature.fox.coffee_counter.data.repository.UserRepository
import org.feature.fox.coffee_counter.ui.common.showAchievementNotification
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Checks if the user got a new achievement and generates it
 *
 * @property userRepository
 * @property preference
 */
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

    /**
     * Generates achievements for paying a total of 100€, 1000€ and 10000€.
     * @param purchases List of purchases.
     * @param achievements List of already received achievements.
     */
    private suspend fun moneySpenderAchievement(
        purchases: List<Purchase>,
        achievements: List<Achievement>
    ) {
        val rookieAchievementName = "Rookie numbers"
        val rookieAchievementDescription = "You only spent 100€"

        val richBitchAchievementName = "Rich Bitch"
        val richBitchAchievementDescription = "You spent more then 1000€, not bad"

        val theOnePercentAchievementName = "You are the 1%"
        val theOnePercentAchievementDescription = "You spent more then 10000€, wtf"

        val rookieAchievementReceived = achievements.any { it.name == rookieAchievementName }
        val richBitchAchievementReceived = achievements.any { it.name == richBitchAchievementName }
        val theOnePercentAchievementReceived =
            achievements.any { it.name == theOnePercentAchievementName }

        var totalSpentMoney = 0.0

        if (!rookieAchievementReceived && !richBitchAchievementReceived && !theOnePercentAchievementReceived) {
            purchases.forEach { purchase ->
                totalSpentMoney += purchase.totalValue
            }
        }

        if (!rookieAchievementReceived && totalSpentMoney <= -100) {
            val cashAchievement = Achievement(
                name = rookieAchievementName,
                userId = preference.getTag(BuildConfig.USER_ID),
                timestamp = System.currentTimeMillis() / 1000,
                description = rookieAchievementDescription,
                icon = R.drawable.rookie_numbers
            )
            userRepository.insertAchievementDb(cashAchievement)
            showAchievementNotification(cashAchievement)
        }

        if (!richBitchAchievementReceived && totalSpentMoney <= -1000) {
            val cashAchievement = Achievement(
                name = richBitchAchievementName,
                userId = preference.getTag(BuildConfig.USER_ID),
                timestamp = System.currentTimeMillis() / 1000,
                description = richBitchAchievementDescription,
                icon = R.drawable.rich_bitch
            )
            userRepository.insertAchievementDb(cashAchievement)
            showAchievementNotification(cashAchievement)
        }

        if (!theOnePercentAchievementReceived && totalSpentMoney <= -10000) {
            val cashAchievement = Achievement(
                name = theOnePercentAchievementName,
                userId = preference.getTag(BuildConfig.USER_ID),
                timestamp = System.currentTimeMillis() / 1000,
                description = theOnePercentAchievementDescription,
                icon = R.drawable.the_one_percent
            )
            userRepository.insertAchievementDb(cashAchievement)
            showAchievementNotification(cashAchievement)
        }
    }

    /**
     * Generates an achievement for each available item.
     * @param purchases List of purchases.
     * @param achievements List of already received achievements.
     * @param items List of available Items.
     */
    private suspend fun junkieAchievements(
        purchases: List<Purchase>,
        achievements: List<Achievement>,
        items: List<Item>,
    ) {
        items.forEach { item ->
            val achievementName = "${item.name} junkie"
            val achievementDescription =
                "You drank over 100 ${item.name}"
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

    /**
     * Generates an achievement for buying between 0:00 and 4:00.
     * @param purchases List of purchases.
     * @param achievements List of already received achievements.
     */
    private suspend fun nightPurchaseAchievement(
        purchases: List<Purchase>,
        achievements: List<Achievement>,
    ) {
        val achievementName = "Sleep is for the weak"
        val achievementDescription = "You bought a drink in the night"
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
