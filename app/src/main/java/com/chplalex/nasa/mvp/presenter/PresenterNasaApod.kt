package com.chplalex.nasa.mvp.presenter

import android.util.Log
import android.view.View
import com.chplalex.nasa.BuildConfig.NASA_API_KEY
import com.chplalex.nasa.R
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
    private val disposable: CompositeDisposable,
    @Named("IO")
    private val ioScheduler: Scheduler,
    @Named("UI")
    private val uiScheduler: Scheduler
) :
    MvpPresenter<IViewNasaApod>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadData(Date().nasaPatternThisDay())
        viewState.setChipGroupListener(this::onCheckedChange)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }

    private fun onCheckedChange(group: ChipGroup, checkedId: Int) {
        val date = when (checkedId) {
            R.id.nasa_apod_chip_today -> Date().nasaPatternThisDay()
            R.id.nasa_apod_chip_week_ago -> Date().nasaPatternWeekAgo()
            R.id.nasa_apod_chip_month_ago -> Date().nasaPatternMonthAgo()
            R.id.nasa_apod_chip_year_ago -> Date().nasaPatternYearAgo()
            else -> Date().nasaPatternThisDay()
        }
        Log.d(TAG, "checkedId = $checkedId, date = $date")
        loadData(date)
    }

    private fun loadData(date: String) {
        viewState.setProgressVisibility(View.VISIBLE)
        disposable.add(
            nasaApi.nasaLoadApod(date, NASA_API_KEY)
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe({
                    viewState.setProgressVisibility(View.INVISIBLE)
                    when (it.mediaType) {
                        "image" -> viewState.setImage(it.url)
                        "video" -> {
                            viewState.setVideo(it.url)
                        }
                        else -> {
                            viewState.setDefaultImage()
                            viewState.showError("Unknown media type", Exception(it.mediaType))
                        }
                    }
                    viewState.setTitle(it.title)
                    viewState.setExplanation(it.explanation)
                }, {
                    viewState.setProgressVisibility(View.INVISIBLE)
                    viewState.showError("NASA API error", it)
                })
        )
    }
}