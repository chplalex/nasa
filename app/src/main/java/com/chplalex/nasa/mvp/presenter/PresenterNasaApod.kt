package com.chplalex.nasa.mvp.presenter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
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
    private val context: Context,
    private val disposable: CompositeDisposable,
    @Named("IO")
    private val ioScheduler: Scheduler,
    @Named("UI")
    private val uiScheduler: Scheduler
) :
    MvpPresenter<IViewNasaApod>() {

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
        Log.d(TAG, "onCheckedChange(), checkedId = $checkedId")
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

    private fun loadData(date: String) {
        viewState.setProgressVisibility(View.VISIBLE)
        disposable.add(
            nasaApi.nasaLoadApod(date)
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe({
                    viewState.setProgressVisibility(View.INVISIBLE)
                    when (it.mediaType) {
                        "image" -> viewState.setImage(it.url)
                        "video" -> {
                            viewState.setImage(it.url.youtubeThumbUrl())
                            showVideo(it.url)
                        }
                        else -> {
                            viewState.setErrorImage()
                            viewState.showError("Unknown media type", Exception(it.mediaType))
                        }
                    }
                    viewState.setTitle(it.title)
                    viewState.setExplanation(it.explanation)
                }, {
                    viewState.setProgressVisibility(View.INVISIBLE)
                    viewState.setErrorImage()
                    viewState.showError("NASA API error", it)
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
}