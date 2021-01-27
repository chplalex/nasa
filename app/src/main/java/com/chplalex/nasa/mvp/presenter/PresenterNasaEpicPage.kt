package com.chplalex.nasa.mvp.presenter

import android.util.Log
import com.chplalex.nasa.mvp.model.NasaEpicItem
import com.chplalex.nasa.mvp.view.IViewNasaEpicPage
import com.chplalex.nasa.utils.TAG
import com.chplalex.nasa.utils.systemPatternThisTime
import moxy.MvpPresenter
import javax.inject.Inject

class PresenterNasaEpicPage @Inject constructor(

) :
    MvpPresenter<IViewNasaEpicPage>() {

    private var item: NasaEpicItem? = null

    fun onLoadData(item: NasaEpicItem?) {
        this.item = item
        item?.let {
            viewState.hideViews()
            viewState.setImage(it.imageUrl())
        }
    }

    fun onImageLoadFailed() {
        Log.d(TAG, "onImageLoadFailed()")
    }

    fun onImageLoadSuccess() {
        Log.d(TAG, "onImageLoadSuccess()")
        item?.let {
            viewState.setCaption(it.caption)
            viewState.setTime(it.timeStamp.systemPatternThisTime())
            viewState.showViews()
        }
    }

}