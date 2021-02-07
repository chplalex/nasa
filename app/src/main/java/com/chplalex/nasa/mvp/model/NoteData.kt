package com.chplalex.ui.note

import android.os.Parcelable
import com.chplalex.nasa.mvp.model.Note
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NoteData(val note: Note, val isDeleted: Boolean = false) : Parcelable