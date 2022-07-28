package org.feature.fox.coffee_counter.util

import com.github.mikephil.charting.formatter.ValueFormatter
import org.feature.fox.coffee_counter.BuildConfig
import java.text.SimpleDateFormat
import java.util.*

class DateTimeFormatter : ValueFormatter() {

    override fun getFormattedValue(value: Float): String {
        val sdf = SimpleDateFormat(BuildConfig.DATE_PATTERN, Locale.GERMAN)
        return sdf.format(value.toLong())
    }
}
