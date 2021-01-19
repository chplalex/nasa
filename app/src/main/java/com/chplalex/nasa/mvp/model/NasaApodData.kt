package com.chplalex.nasa.mvp.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class NasaApodData(
    @SerializedName("code") @Expose
    val code: Int,
    @SerializedName("msg") @Expose
    val msg: String,
    @SerializedName("date") @Expose
    val date: String,
    @SerializedName("explanation") @Expose
    val explanation: String,
    @SerializedName("hdurl") @Expose
    val hdUrl: String,
    @SerializedName("media_type") @Expose
    val mediaType: String,
    @SerializedName("service_version") @Expose
    val serviceVersion: String,
    @SerializedName("title") @Expose
    val title: String,
    @SerializedName("url") @Expose
    val url: String
)
