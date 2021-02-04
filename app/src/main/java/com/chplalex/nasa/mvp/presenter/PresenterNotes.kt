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
        val noteInList = findInList(noteData.note)

        if (isDeleted) {

            if (noteInList == null) {
                return@with
            } else {
                notes.remove(noteInList)
            }

        } else {

            if (noteInList == null) {
                notes.add(noteData.note)
            } else {
                noteInList.copy(
                    title = noteData.note.title,
                    body = noteData.note.body,
                    color = noteData.note.color,
                    lastChanged = noteData.note.lastChanged
                )
            }
        }

        viewState.notifyNotesChanged()
    }

    private fun findInList(note: Note): Note? = try {
        notes.first { note.id == it.id }
    } catch (exception: Exception) {
        null
    }
}