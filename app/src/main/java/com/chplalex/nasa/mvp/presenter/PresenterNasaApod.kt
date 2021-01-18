package com.chplalex.nasa.mvp.presenter

import com.chplalex.nasa.BuildConfig.NASA_API_KEY
import com.chplalex.nasa.mvp.view.IViewNasaApod
import com.chplalex.nasa.service.api.NasaApi
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import moxy.MvpPresenter
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
        loadData()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
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