package com.chplalex.nasa.mvp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Note(
    val id : String = UUID.randomUUID().toString(),
    var title: String = "",
    var body: String = "",
    val color: Color = Color.WHITE,
    var lastChanged: Date = Date()) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        return id == (other as Note).id
    }

    override fun hashCode(): Int = id.hashCode()
}
