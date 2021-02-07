package com.chplalex.nasa.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.chplalex.nasa.R
import com.chplalex.nasa.mvp.model.Note
import com.chplalex.nasa.mvp.presenter.PresenterNotes
import com.chplalex.nasa.mvp.view.IViewNotes
import com.chplalex.nasa.ui.App
import com.chplalex.nasa.ui.view.NotesAdapter
import com.chplalex.ui.note.NoteData
import moxy.MvpAppCompatActivity
import moxy.MvpAppCompatDialogFragment
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

private const val REQUEST_CODE_NOTE = 4444

class NotesActivity : MvpAppCompatActivity(R.layout.activity_notes),
    IViewNotes,
    NotesAdapter.OnClickListener {

    @Inject
    lateinit var providerPresenter: Provider<PresenterNotes>

    private val presenter by moxyPresenter {
        providerPresenter.get()
    }

    private lateinit var adapter: NotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        App.instance.activityComponent?.inject(this)
        super.onCreate(savedInstanceState)
        initToolbar()
        initNotes()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_NOTE && resultCode == RESULT_OK) {
            data?.getParcelableExtra<NoteData>(NOTE_ACTIVITY_RESULT)?.let {
                presenter.onNoteResult(it)
            }
        }
    }

    private fun initNotes() {
        adapter = NotesAdapter(this)
        findViewById<RecyclerView>(R.id.notes_recycler_view).adapter = adapter
    }

    private fun initToolbar() {
        setSupportActionBar(findViewById(R.id.notes_toolbar))
    }

    override fun setNotes(notes: List<Note>) {
        adapter.notes = notes
    }

    override fun notifyNotesChanged() {
        adapter.notifyDataSetChanged()
    }

    override fun notifyNoteInserted(position: Int) {
        adapter.notifyItemInserted(position)
    }

    override fun notifyNoteChanged(position: Int) {
        adapter.notifyItemChanged(position)
    }

    override fun notifyNoteRemoved(position: Int) {
        adapter.notifyItemRemoved(position)
    }

    override fun onNoteClick(note: Note) {
        Intent(this, NoteActivity::class.java).apply {
            putExtra(NOTE_ACTIVITY_ARGS, note)
            startActivityForResult(this, REQUEST_CODE_NOTE)
        }
    }
}