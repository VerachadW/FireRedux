package me.lazmaid.fireredux.model

import paperparcel.PaperParcel
import paperparcel.PaperParcelable

/**
 * Created by VerachadW on 12/31/2016 AD.
 */

@PaperParcel
data class Note(var id: String = "note-${System.currentTimeMillis()}", var title: String = "", var content: String = "") : PaperParcelable {
    companion object {
        @JvmField val CREATOR = PaperParcelNote.CREATOR
    }
}