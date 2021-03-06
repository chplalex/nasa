package com.chplalex.nasa.mvp.presenter

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.chplalex.nasa.mvp.view.IViewWiki
import com.chplalex.nasa.ui.App
import com.chplalex.nasa.utils.WIKI_BASE_URL
import moxy.MvpPresenter
import javax.inject.Inject

class PresenterWiki @Inject constructor(
    private val context: Context
) :
    MvpPresenter<IViewWiki>() {

    fun onEndIconPressed(text: String) = Intent(Intent.ACTION_VIEW).also {
        it.data = Uri.parse(WIKI_BASE_URL + text)
        context.startActivity(it)
    }
}