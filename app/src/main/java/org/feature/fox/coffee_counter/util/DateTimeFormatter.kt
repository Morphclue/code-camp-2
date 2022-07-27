package org.feature.fox.coffee_counter.util

import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat

class DateTimeFormatter : ValueFormatter() {

    override fun getFormattedValue(value: Float): String {
        val sdf = SimpleDateFormat("W/MM")
        return sdf.format(value.toLong())
    }
}
