package com.chplalex.nasa.mvp.view

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

@AddToEndSingle
interface IViewNasaApod : MvpView {
    fun setTitle(title: String)
    fun setImage(url: String)
    fun setExplanation(explanation: String)
    fun showError(msg: String, error: Throwable)
}