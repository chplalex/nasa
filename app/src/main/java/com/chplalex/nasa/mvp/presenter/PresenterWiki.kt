package com.chplalex.nasa.mvp.presenter

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.chplalex.nasa.mvp.view.IViewWiki
import com.chplalex.nasa.ui.App
import com.chplalex.nasa.utils.WIKI_BASE_URL
import moxy.MvpPresenter
import javax.inject.Inject

class PresenterWiki : MvpPresenter<IViewWiki>() {

    @Inject
    lateinit var context: Context

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        App.instance.activityComponent?.inject(this)
    }

    fun onEndIconPressed(text: String) = Intent(Intent.ACTION_VIEW).also {
        it.data = Uri.parse(WIKI_BASE_URL + text)
        context.startActivity(it)
    }
}