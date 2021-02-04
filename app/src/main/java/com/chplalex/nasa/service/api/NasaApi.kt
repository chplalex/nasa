package com.chplalex.nasa.service.api

import com.chplalex.nasa.BuildConfig
import com.chplalex.nasa.mvp.model.NasaApodData
import com.chplalex.nasa.mvp.model.NasaEpicItem
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

const val NASA_BASE_URL = "https://api.nasa.gov/"
const val NASA_DATE_PATTERN = "yyyy-MM-dd"
const val NASA_EPIC_IMAGE_DATE_PATTERN = "yyyy/MM/dd"
const val NASA_EPIC_TIMESTAMP_PATTERN = "yyyy-MM-dd HH:mm:ss"
const val NOTE_DATE_PATTERN = "HH:mm dd-MM-yyyy"

interface NasaApi {

    @GET("planetary/apod")
    fun nasaLoadApod(
        @Query("date") date: String,
        @Query("api_key") apiKey: String = BuildConfig.NASA_API_KEY
    ): Single<NasaApodData>

    @GET("EPIC/api/natural/date/")
    fun nasaLoadEpicListLastDate(
        @Query("api_key") apiKey: String = BuildConfig.NASA_API_KEY
    ): Single<List<NasaEpicItem>>

    @GET("EPIC/api/natural/date/{date}")
    fun nasaLoadEpicListByDate(
        @Path("date") date: String,
        @Query("api_key") apiKey: String = BuildConfig.NASA_API_KEY
    ): Single<List<NasaEpicItem>>
}