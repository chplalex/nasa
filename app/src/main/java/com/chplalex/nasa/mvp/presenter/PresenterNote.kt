package com.chplalex.nasa.mvp.presenter

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
            viewState.setToolbarColor(color)
            viewState.setTitle(title)
            viewState.setBody(body)
        }
    }

    fun deleteNote() {
        note.lastChanged = Date()
        viewState.finishWithResult(NoteData(note, isDeleted = true))
    }

    fun saveNote(note: Note) {
        note.lastChanged = Date()
        viewState.finishWithResult(NoteData(note, isDeleted = false))
    }

    fun titleChanged(title: String) {
        note.title = title
    }

    fun bodyChanged(body: String) {
        note.body = body
    }
}