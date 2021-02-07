package com.chplalex.nasa.mvp.presenter

import com.chplalex.nasa.mvp.model.Color
import com.chplalex.nasa.mvp.model.Note
import com.chplalex.nasa.mvp.view.IViewNote
import com.chplalex.ui.note.NoteData
import moxy.MvpPresenter
import java.util.*
import javax.inject.Inject

class PresenterNote @Inject constructor() : MvpPresenter<IViewNote>() {

    private lateinit var note: Note

    fun initByArgs(arg: Note?) {
        if (arg == null) {
            note = Note()
        } else {
            note = arg
        }
        with(note) {
            viewState.setColor(color)
            viewState.setTitle(title)
            viewState.setBody(body)
        }
    }

    fun delete() {
        viewState.finishWithResult(NoteData(note, isDeleted = true))
    }

    fun home() {
        viewState.finishWithResult(NoteData(note, isDeleted = false))
    }

    fun titleChanged(title: String) {
        note = note.copy(title = title, lastChanged = Date())
    }

    fun bodyChanged(body: String) {
        note = note.copy(body = body, lastChanged = Date())
    }

    fun colorChanged(color: Color) {
        note = note.copy(color = color, lastChanged = Date())
        viewState.setColor(color)
    }
}