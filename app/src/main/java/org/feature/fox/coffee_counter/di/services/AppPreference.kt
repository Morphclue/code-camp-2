package org.feature.fox.coffee_counter.di.services

import android.content.Context
import androidx.preference.PreferenceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * The AppPreference class is used to store and retrieve data from SharedPreferences.
 *
 * @constructor
 * Given the application context, the AppPreference constructor initializes the SharedPreferences.
 *
 * @param context The application context.
 */
@Singleton
class AppPreference @Inject constructor(@ApplicationContext context: Context) {
    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    /**
     * Retrieves the value of the specified key from SharedPreferences.
     *
     * @param name The name of the key.
     * @return The value of the key as a String or an empty String if the key was not found.
     */
    fun getTag(name: String): String {
        return preferences.getString(name, "") ?: return ""
    }

    /**
     * Retrieves the value of the specified key from SharedPreferences.
     *
     * @param name The name of the key.
     * @param default The default value to return if the key was not found.
     * @return The value of the key as a Boolean or the default value if the key was not found.
     */
    fun getTag(name: String, default: Boolean): Boolean {
        return preferences.getBoolean(name, default)
    }

    /**
     * Stores the value of the specified key in SharedPreferences (String).
     *
     * @param name The name of the key.
     * @param content The value of the key as a String.
     */
    fun setTag(name: String, content: String) {
        preferences.edit().putString(name, content).apply()
    }

    /**
     * Stores the value of the specified key in SharedPreferences (Boolean).
     *
     * @param name The name of the key.
     * @param value The value of the key as a Boolean.
     */
    fun setTag(name: String, value: Boolean) {
        preferences.edit().putBoolean(name, value).apply()
    }

    /**
     * Removes the value of the specified key from SharedPreferences.
     *
     * @param name The name of the key.
     */
    fun removeTag(name: String) {
        preferences.edit().remove(name).apply()
    }
}
