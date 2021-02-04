package com.chplalex.nasa.ui.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.chplalex.nasa.R
import com.chplalex.nasa.mvp.model.Color
import com.chplalex.nasa.mvp.model.Color.*
import com.chplalex.nasa.mvp.model.Note
import com.chplalex.nasa.utils.noteDatePatternString
import kotlinx.android.extensions.LayoutContainer

class NotesAdapter(
    private val onClickListener: OnClickListener? = null
) :
    RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    var notes: List<Note> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = NoteViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.item_note,
            parent,
            false
        )
    )

    override fun getItemCount() = notes.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) =
        holder.bind(notes[position])

    inner class NoteViewHolder(
        override val containerView: View
    ) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(note: Note) = with(note) {
            itemView.findViewById<TextView>(R.id.txtTitle).text = title
            itemView.findViewById<TextView>(R.id.txtBody).text = body
            itemView.findViewById<TextView>(R.id.txtDate).text = lastChanged.noteDatePatternString()
            itemView.setBackgroundColor(color.getColorInt(itemView.context))
            itemView.setOnClickListener { onClickListener?.onNoteClick(this) }
        }
    }

    interface OnClickListener {
        abstract fun onNoteClick(note: Note)
    }

    private fun Color.getColorInt(context: Context): Int = getColor(context, getColorRes())

    private fun Color.getColorRes() = when (this) {
        WHITE -> R.color.color_white
        VIOLET -> R.color.color_violet
        YELLOW -> R.color.color_yellow
        RED -> R.color.color_red
        PINK -> R.color.color_pink
        GREEN -> R.color.color_green
        BLUE -> R.color.color_blue
    }
}