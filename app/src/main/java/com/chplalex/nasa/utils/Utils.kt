package com.chplalex.nasa.utils

import android.net.Uri
import android.util.Log
import com.chplalex.nasa.service.api.NASA_DATE_PATTERN
import com.chplalex.nasa.service.api.NASA_EPIC_IMAGE_DATE_PATTERN
import com.chplalex.nasa.service.api.NASA_EPIC_TIMESTAMP_PATTERN
import java.text.SimpleDateFormat
import java.util.*

const val TAG = "NASA"
const val WIKI_BASE_URL = "https://ru.wikipedia.org/wiki/"
const val PREFS_FILE_NAME = "nasa.prefs"
const val PREFS_THEME_ID = "prefs_theme_id"
const val THEME_ID_SYSTEM = 1
const val THEME_ID_LIGHT = 2
const val THEME_ID_DARK = 3
const val THEME_ID_CUSTOM = 4


fun Date.add(field: Int, amount: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.add(field, amount)
    return calendar.time
}

fun Date.nasaDatePatternString(): String =
    SimpleDateFormat(NASA_DATE_PATTERN, Locale.getDefault()).format(this)

fun Date.nasaDatePatternThisDay() = nasaDatePatternString()
fun Date.nasaDatePatternWeekAgo() = add(Calendar.DATE, -7).nasaDatePatternString()
fun Date.nasaDatePatternMonthAgo() = add(Calendar.MONTH, -1).nasaDatePatternString()
fun Date.nasaDatePatternYearAgo() = add(Calendar.YEAR, -1).nasaDatePatternString()

fun Date.systemPatternThisDay() = SimpleDateFormat.getDateInstance().format(this)
fun Date.systemPatternThisTime() =
    SimpleDateFormat.getDateTimeInstance().format(this) +
            " " +
            SimpleDateFormat.getDateInstance().format(this)

fun Date.nasaEpicImageDatePatternString(): String =
    SimpleDateFormat(NASA_EPIC_IMAGE_DATE_PATTERN, Locale.getDefault()).format(this)

fun String.youtubeThumbUrl(): String {
    val uri = Uri.parse(this)
    val protocol = uri.scheme
    val server = uri.authority
    val path = uri.path
    Log.d(TAG, "url = $this, protocol = $protocol, server = $server, path = $path")
    val resultUrl = "https://img.youtube.com/vi" + path + "/maxresdefault.jpg"
    Log.d(TAG, "resultUrl = $resultUrl")
    return resultUrl
}


