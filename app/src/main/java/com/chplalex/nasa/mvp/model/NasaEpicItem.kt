package com.chplalex.nasa.mvp.model

import android.os.Parcelable
import com.chplalex.nasa.BuildConfig
import com.chplalex.nasa.service.api.NASA_BASE_URL
import com.chplalex.nasa.service.api.NASA_EPIC_TIMESTAMP_PATTERN
import com.chplalex.nasa.utils.nasaEpicImageDatePatternString
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NasaEpicItem(
    @SerializedName("caption") @Expose
    val caption: String,
    @SerializedName("image") @Expose
    val image: String,
    @SerializedName("date") @Expose
    val timeString: String,
    var timeStamp: Date
) : Parcelable {

    fun syncTime() {
        timeStamp = SimpleDateFormat(NASA_EPIC_TIMESTAMP_PATTERN, Locale.getDefault()).parse(timeString)
    }

    fun imageUrl() = String.format("%sEPIC/archive/natural/%s/png/%s.png?api_key=%s",
        NASA_BASE_URL,
        timeStamp.nasaEpicImageDatePatternString(),
        image,
        BuildConfig.NASA_API_KEY)
}
