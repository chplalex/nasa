package com.chplalex.nasa.utils

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import com.chplalex.nasa.service.api.NASA_DATE_PATTERN
import java.text.SimpleDateFormat
import java.util.*

const val TAG = "NASA"
const val SP_NAME = "nasa.prefs"
const val WIKI_BASE_URL = "https://ru.wikipedia.org/wiki/"

fun Date.add(field: Int, amount: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.add(field, amount)
    return calendar.time
}

fun Date.nasaPatternString(): String = SimpleDateFormat(NASA_DATE_PATTERN, Locale.getDefault()).format(this)
fun Date.nasaPatternThisDay() = nasaPatternString()
fun Date.nasaPatternWeekAgo() = add(Calendar.DATE, -7).nasaPatternString()
fun Date.nasaPatternMonthAgo() = add(Calendar.MONTH, -1).nasaPatternString()
fun Date.nasaPatternYearAgo() = add(Calendar.YEAR, -1).nasaPatternString()

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


