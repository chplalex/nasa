package com.chplalex.nasa.mvp.view

import com.chplalex.nasa.mvp.model.Note
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

@AddToEndSingle
interface IViewNotes : MvpView {
    fun setNotes(notes: List<Note>)
    fun notifyNotesChanged()
    fun notifyNoteInserted(position: Int)
    fun notifyNoteChanged(position: Int)
    fun notifyNoteRemoved(position: Int)
}