package com.chplalex.nasa.mvp.presenter

import android.util.Log
import com.chplalex.nasa.mvp.view.IViewNasaEpicContainer
import com.chplalex.nasa.service.api.NasaApi
import com.chplalex.nasa.utils.TAG
import com.chplalex.nasa.utils.nasaDatePatternThisDay
import com.chplalex.nasa.utils.systemPatternThisDay
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import moxy.MvpPresenter
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Named

class PresenterNasaEpicContainer @Inject constructor(
    private val nasaApi: NasaApi,
    @Named("IO")
    private val ioScheduler: Scheduler,
    @Named("UI")
    private val uiScheduler: Scheduler,
    private val disposable: CompositeDisposable
) : MvpPresenter<IViewNasaEpicContainer>() {

    private var chipDate = Date()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        initData()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }

    private fun initData() {
        disposable.add(
            nasaApi.nasaLoadEpicListLastDate()
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe({
                    for (item in it) {
                        item.syncTime()
                        Log.d(TAG, "timeString = ${item.timeString}, timeDate = ${SimpleDateFormat.getDateTimeInstance().format(item.timeStamp)}")
                    }
                    viewState.setViewPager(it)
                    viewState.setChipDate(it[0].timeStamp.systemPatternThisDay())
                }, {
                    viewState.showError("Error loading NASA EPIC images list", it)
                })
        )
    }

    private fun loadData() {
        disposable.add(
            nasaApi.nasaLoadEpicListByDate(chipDate.nasaDatePatternThisDay())
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe({
                    for (item in it) {
                        item.syncTime()
                        Log.d(TAG, "caption = ${item.caption}, image = {${item.image}}, date = {${item.timeString}}")
                    }
                    viewState.setViewPager(it)
                    viewState.setChipDate(it[0].timeStamp.systemPatternThisDay())
                }, {
                    viewState.showError("Error loading NASA EPIC images list", it)
                })
        )
    }

    fun onChipDateClick() {
        viewState.requestDate(chipDate)
    }

    fun onDateSelected(date: Date) {
        chipDate = date
        loadData()
    }
}