package org.feature.fox.coffee_counter.util

import com.github.mikephil.charting.formatter.ValueFormatter
import org.feature.fox.coffee_counter.BuildConfig
import java.text.SimpleDateFormat
import java.util.*

/**
 * Class that handles formatting of DateTime.
 */
class DateTimeFormatter : ValueFormatter() {

    /**
     * Formats the given date.
     *
     * @param value the date to format.
     * @return the formatted date.
     */
    override fun getFormattedValue(value: Float): String {
        val sdf = SimpleDateFormat(BuildConfig.LINE_CHART_PATTERN, Locale.GERMAN)
        return sdf.format(value.toLong())
    }
}
