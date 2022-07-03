package org.feature.fox.coffee_counter.di.services

import android.content.Context
import androidx.preference.PreferenceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppPreference @Inject constructor(@ApplicationContext context: Context) {
    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun getTag(name: String): String {
        return preferences.getString(name, "") ?: return ""
    }

    fun getTag(name: String, default: Boolean): Boolean {
        return preferences.getBoolean(name, default)
    }

    fun setTag(name: String, content: String) {
        preferences.edit().putString(name, content).apply()
    }

    fun setTag(name: String, value: Boolean) {
        preferences.edit().putBoolean(name, value).apply()
    }

    fun removeTag(name: String) {
        preferences.edit().remove(name).apply()
    }
}
