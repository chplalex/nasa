package com.chplalex.nasa.service.api

import com.chplalex.nasa.mvp.model.NasaApodData
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

const val NASA_BASE_URL = "https://api.nasa.gov/"

interface NasaApi {

    @GET("planetary/apod")
    fun nasaLoadApod(
        @Query("api_key") apiKey: String
    ) : Single<NasaApodData>
}