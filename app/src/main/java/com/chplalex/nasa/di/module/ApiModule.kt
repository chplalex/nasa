package com.chplalex.nasa.di.module

import com.chplalex.nasa.BuildConfig
import com.chplalex.nasa.di.scope.AppScope
import com.chplalex.nasa.service.api.NASA_BASE_URL
import com.chplalex.nasa.service.api.NasaApi
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
class ApiModule {

    @AppScope
    @Provides
    fun nasaApi(okClient: OkHttpClient) : NasaApi = Retrofit.Builder()
        .baseUrl(NASA_BASE_URL)
        .client(okClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(NasaApi::class.java)

    @AppScope
    @Provides
    fun okHttpClient(loggingInterceptor: HttpLoggingInterceptor) = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    @AppScope
    @Provides
    fun httpLoggingInterceptor() = HttpLoggingInterceptor().also {
        it.level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

}