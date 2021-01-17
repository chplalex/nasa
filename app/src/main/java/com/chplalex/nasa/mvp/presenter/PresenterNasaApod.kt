package com.chplalex.nasa.mvp.presenter

import android.content.Context
import com.chplalex.nasa.BuildConfig.NASA_API_KEY
import com.chplalex.nasa.mvp.view.IViewNasaApod
import com.chplalex.nasa.service.api.NasaApi
import com.chplalex.nasa.ui.App.Companion.instance
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import moxy.MvpPresenter
import javax.inject.Inject
import javax.inject.Named

class PresenterNasaApod() : MvpPresenter<IViewNasaApod>() {

    @Inject
    lateinit var nasaApi: NasaApi

    @Inject
    lateinit var context: Context

    @Inject
    lateinit var disposable: CompositeDisposable

    @Inject
    @Named("IO")
    lateinit var ioScheduler: Scheduler

    @Inject
    @Named("UI")
    lateinit var uiScheduler: Scheduler


    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        instance.activityComponent?.inject(this)
        loadData()
    }

    private var disp: Disposable? = null

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
        disp?.dispose()
    }

    private fun loadData() {
        disposable.add(
            nasaApi.nasaLoadApod(NASA_API_KEY)
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe({
                    when (it.mediaType) {
                        "image" -> viewState.setImage(it.url)
                        else -> viewState.showError("Unknown media type", Exception(it.mediaType))
                    }
                    viewState.setTitle(it.title)
                    viewState.setExplanation(it.explanation)
                }, {
                    viewState.showError("", it)
                })
        )
    }
}