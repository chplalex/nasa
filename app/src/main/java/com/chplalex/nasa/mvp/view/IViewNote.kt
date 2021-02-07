package com.chplalex.nasa.mvp.view

import com.chplalex.nasa.mvp.model.Color
import com.chplalex.ui.note.NoteData
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

@AddToEndSingle
interface IViewNote : MvpView {
    fun setColor(color: Color)
    fun setTitle(title: String)
    fun setBody(body: String)
    fun finishWithResult(noteData: NoteData)
}