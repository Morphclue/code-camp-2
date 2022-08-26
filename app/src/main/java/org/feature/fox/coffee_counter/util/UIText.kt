package org.feature.fox.coffee_counter.util

import android.content.Context
import androidx.annotation.StringRes

/**
 * A class to provide string resources.
 * reference: https://www.youtube.com/watch?v=mB1Lej0aDus
 */
sealed class UIText {
    /**
     * Data class to provide [UIText].
     * @property value String to be displayed.
     */
    data class DynamicString(val value: String) : UIText()

    /**
     * Class that provides string resources.
     *
     * @property resId Resource ID.
     * @property args Additional Arguments.
     */
    class StringResource(
        @StringRes val resId: Int,
        vararg val args: Any,
    ) : UIText()

    /**
     * Returns the string resource for the given [context].
     *
     * @param context Context to get the string resource from.
     * @return [DynamicString] or [StringResource] depending on the type of [UIText].
     */
    fun asString(context: Context): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> context.getString(resId, *args)
        }
    }
}
