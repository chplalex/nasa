package com.chplalex.nasa.mvp.view

import com.chplalex.nasa.mvp.model.NasaEpicItem
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import java.util.*

@AddToEndSingle
interface IViewNasaEpicContainer : MvpView {
    fun setChipDate(text: CharSequence)
    fun requestDate(chipDate: Date)
    fun showError(title: String, error: Throwable)
    fun setViewPager(list: List<NasaEpicItem>)
}