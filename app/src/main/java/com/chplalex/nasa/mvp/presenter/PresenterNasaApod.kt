package com.chplalex.nasa.mvp.presenter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import com.chplalex.nasa.R
import com.chplalex.nasa.mvp.model.NasaApodData
import com.chplalex.nasa.mvp.view.IViewNasaApod
import com.chplalex.nasa.service.api.NasaApi
import com.chplalex.nasa.utils.*
import com.google.android.material.chip.ChipGroup
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import moxy.MvpPresenter
import java.util.*
import javax.inject.Inject
import javax.inject.Named

class PresenterNasaApod @Inject constructor(
    private val nasaApi: NasaApi,
    private val context: Context,
    private val disposable: CompositeDisposable,
    @Named("IO")
    private val ioScheduler: Scheduler,
    @Named("UI")
    private val uiScheduler: Scheduler
) :
    MvpPresenter<IViewNasaApod>() {

    private var data: NasaApodData? = null
    private var checkedId: Int = -1

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadData(Date().nasaDatePatternThisDay())
        viewState.setChipGroupListener(this::onCheckedChange)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }

    private fun onCheckedChange(group: ChipGroup, checkedId: Int) {
        this.checkedId = checkedId
        viewState.hideAnimatedViews()
    }

    private fun loadData(date: String) {
        disposable.add(
            nasaApi.nasaLoadApod(date)
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe({
                    Log.d(
                        TAG,
                        "1. NASA APOD data load success: url = ${it.url}, title = ${it.title}, explanation = ${it.explanation}"
                    )
                    data = it
                    when (it.mediaType) {
                        "image" -> viewState.setImage(it.url)
                        "video" -> viewState.setImage(it.url.youtubeThumbUrl())
                        else -> viewState.setErrorImage()
                    }
                }, {
                    viewState.setErrorImage()
                    viewState.showError("NASA API error", it)
                    viewState.showAnimatedViews()
                })
        )
    }

    private fun showVideo(url: String) = Intent(Intent.ACTION_VIEW).also {
        it.data = Uri.parse(url)
        context.startActivity(it)
    }

    fun onDialogButtonPressed() {
        viewState.setChipToday()
    }

    fun onImageLoadFailed() {
        Log.d(TAG, "onImageLoadFailed()")
        viewState.showAnimatedViews()
    }

    fun onImageLoadSuccess() = data?.let {
        Log.d(
            TAG,
            "2. NASA APOD data load success: url = ${it.url}, title = ${it.title}, explanation = ${it.explanation}"
        )
        viewState.setTitle(it.title)
        viewState.setExplanation(it.explanation)
        viewState.showAnimatedViews()
        when (it.mediaType) {
            "image" -> return@let
            "video" -> showVideo(it.url)
            else -> viewState.showError("Unknown media type", Exception(it.mediaType))
        }
    }

    fun onViewsHided()  {
        when (checkedId) {
            R.id.nasa_apod_chip_today -> loadData(Date().nasaDatePatternThisDay())
            R.id.nasa_apod_chip_week_ago -> loadData(Date().nasaDatePatternWeekAgo())
            R.id.nasa_apod_chip_month_ago -> loadData(Date().nasaDatePatternMonthAgo())
            R.id.nasa_apod_chip_year_ago -> loadData(Date().nasaDatePatternYearAgo())
            else -> {
                viewState.setChipToday()
            }
        }
    }
}