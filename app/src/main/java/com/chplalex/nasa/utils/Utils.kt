package com.chplalex.nasa.utils

import android.annotation.SuppressLint
import com.chplalex.nasa.service.api.NASA_DATE_PATTERN
import java.text.SimpleDateFormat
import java.util.*

const val TAG = "NASA"
const val SP_NAME = "nasa.prefs"
const val WIKI_BASE_URL = "https://ru.wikipedia.org/wiki/"

fun Date.add(field: Int, amount: Int): Date {
    Calendar.getInstance().apply {
        time = this@add
        add(field, amount)
        return time
    }
}

@SuppressLint("WeekBasedYear")
fun Date.nasaPatternString(): String = SimpleDateFormat(NASA_DATE_PATTERN, Locale.getDefault()).format(this)

fun Date.nasaPatternThisDay() = nasaPatternString()

fun Date.nasaPatternWeekAgo() = add(Calendar.DAY_OF_MONTH, -7).nasaPatternString()

fun Date.nasaPatternMonthAgo() = add(Calendar.MONTH, -1).nasaPatternString()

fun Date.nasaPatternYearAgo() = add(Calendar.YEAR, -1).nasaPatternString()

