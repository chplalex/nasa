package com.chplalex.nasa.mvp.presenter

import com.chplalex.nasa.mvp.model.NasaEpicItem
import com.chplalex.nasa.mvp.view.IViewNasaEpicPage
import com.chplalex.nasa.utils.systemPatternThisTime
import moxy.MvpPresenter
import javax.inject.Inject

class PresenterNasaEpicPage @Inject constructor(

) :
    MvpPresenter<IViewNasaEpicPage>() {

    fun onLoadData(item: NasaEpicItem?) {
        item?.let {
            viewState.setImage(it.imageUrl())
            viewState.setCaption(it.caption)
            viewState.setTime(it.timeStamp.systemPatternThisTime())
        }
    }

}