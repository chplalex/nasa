package com.chplalex.nasa.mvp.view

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.google.android.material.chip.ChipGroup
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

@AddToEndSingle
interface IViewNasaApod : MvpView {
    fun setTitle(title: String)
    fun setImage(url: String)
    fun setVideo(url: String)
    fun setDefaultImage()
    fun setExplanation(explanation: String)
    fun setProgressVisibility(state: Int)
    fun showError(msg: String, error: Throwable)
    fun setChipGroupListener(listener: ChipGroup.OnCheckedChangeListener)
}