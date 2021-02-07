package com.chplalex.nasa.ui.activity

import android.content.Context
import android.content.Intent
import android.os.*
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.chplalex.nasa.R
import com.chplalex.nasa.mvp.model.Color
import com.chplalex.nasa.mvp.model.Note
import com.chplalex.nasa.mvp.presenter.PresenterNote
import com.chplalex.nasa.mvp.view.IViewNote
import com.chplalex.nasa.ui.App
import com.chplalex.ui.note.NoteData
import kotlinx.android.synthetic.main.activity_note.*
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

const val NOTE_ACTIVITY_ARGS = "NOTE ACTIVITY ARGUMENTS"
const val NOTE_ACTIVITY_RESULT = "NOTE ACTIVITY RESULT"

class NoteActivity : MvpAppCompatActivity(R.layout.activity_note), IViewNote {

    @Inject
    lateinit var providerPresenter: Provider<PresenterNote>

    private val presenter by moxyPresenter {
        providerPresenter.get()
    }

    private var color = Color.WHITE

    private val titleChangeListener = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) {
            presenter.titleChanged(txtNoteTitle.text.toString())
        }
    }

    private val bodyChangeListener = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) {
            presenter.bodyChanged(txtNoteBody.text.toString())
        }
    }

    private val colorChangeListener: (color: Color) -> Unit = { presenter.colorChanged(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        App.instance.activityComponent?.inject(this)
        super.onCreate(savedInstanceState)
        initToolbar()
        initColorPicker()
        initParams()
    }

    private fun initParams() {
        presenter.initByArgs(intent.getParcelableExtra<Note>(NOTE_ACTIVITY_ARGS))
    }

    private fun initToolbar() {
        setSupportActionBar(findViewById(R.id.note_toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initColorPicker() {
        colorPicker.onColorClickListener = colorChangeListener
    }

    override fun onCreateOptionsMenu(menu: Menu?) =
        menuInflater.inflate(R.menu.menu_note, menu).let { true }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_note_palette -> palettePressed().let { true }
            android.R.id.home -> homePressed().let { true }
            R.id.menu_note_delete -> deletePressed().let { true }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun homePressed() {
        presenter.home()
    }

    private fun deletePressed() {
        AlertDialog.Builder(this)
            .setTitle(R.string.delete_note_title)
            .setMessage(R.string.delete_note_message)
            .setPositiveButton(R.string.ok_btn_title) { _, _ -> presenter.delete() }
            .setNegativeButton(R.string.cancel_btn_title) { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun palettePressed() {
        if (colorPicker.isOpen) {
            colorPicker.close()
        } else {
            colorPicker.open()
        }
    }

    override fun setColor(color: Color) {
        note_toolbar.setBackgroundColor(color.getColorInt(this))
    }

    override fun setTitle(title: String) {
        txtNoteTitle.removeTextChangedListener(titleChangeListener)
        txtNoteTitle.setText(title)
        txtNoteTitle.addTextChangedListener(titleChangeListener)
    }

    override fun setBody(body: String) {
        txtNoteBody.removeTextChangedListener(bodyChangeListener)
        txtNoteBody.setText(body)
        txtNoteBody.addTextChangedListener(bodyChangeListener)
    }

    override fun finishWithResult(noteData: NoteData) {
        Intent().also {
            it.putExtra(NOTE_ACTIVITY_RESULT, noteData)
            setResult(RESULT_OK, it)
        }
        finish()
    }

    private fun Color.getColorInt(context: Context): Int =
        ContextCompat.getColor(context, getColorRes())

    private fun Color.getColorRes() = when (this) {
        Color.WHITE -> R.color.color_white
        Color.VIOLET -> R.color.color_violet
        Color.YELLOW -> R.color.color_yellow
        Color.RED -> R.color.color_red
        Color.PINK -> R.color.color_pink
        Color.GREEN -> R.color.color_green
        Color.BLUE -> R.color.color_blue
    }
}