package me.lazmaid.fireredux.model

import paperparcel.PaperParcel
import paperparcel.PaperParcelable

/**
 * Created by VerachadW on 12/31/2016 AD.
 */

@PaperParcel
data class Note(var key: String = "", var id: String = "note-${System.currentTimeMillis()}", var title: String = "", var content: String = "") : PaperParcelable {
    companion object {
        @JvmField val CREATOR = PaperParcelNote.CREATOR
    }

    fun toMap(): Map<String, Any> =
        mapOf("id" to id, "title" to title, "content" to content)
}