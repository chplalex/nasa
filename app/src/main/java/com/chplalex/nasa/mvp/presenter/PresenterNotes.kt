package com.chplalex.nasa.mvp.presenter

import com.chplalex.nasa.mvp.model.Note
import com.chplalex.nasa.mvp.view.IViewNotes
import com.chplalex.nasa.utils.randomNotesList
import com.chplalex.ui.note.NoteData
import moxy.MvpPresenter
import java.util.*
import javax.inject.Inject

class PresenterNotes @Inject constructor() : MvpPresenter<IViewNotes>() {

    private val notes = randomNotesList(15)

    override fun attachView(view: IViewNotes?) {
        super.attachView(view)
        viewState.setNotes(notes)
    }

    fun onNoteResult(noteData: NoteData) = with(noteData) {
        if (isDeleted) {
            notes.findItem(note)?.let { position ->
                notes.removeAt(position)
                viewState.notifyNoteRemoved(position)
            }
        } else {
            notes.findItem(note)?.let { position ->
                notes.removeAt(position)
                notes.add(position, note)
                viewState.notifyNoteChanged(position)
            } ?: let {
                notes.add(0, note)
                viewState.notifyNoteInserted(0)
            }
        }
    }

    private fun List<Note>.findItem(note: Note) : Int? {
        for (i in indices) {
            if (get(i).id == note.id) return i
        }
        return null
    }
}