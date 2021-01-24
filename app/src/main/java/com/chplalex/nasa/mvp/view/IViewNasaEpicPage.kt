package com.chplalex.nasa.mvp.view

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

@AddToEndSingle
interface IViewNasaEpicPage : MvpView {
    fun setImage(url: String)
    fun setCaption(caption: String)
    fun setTime(time: String)
    fun setProgressVisibility(state: Int)
}