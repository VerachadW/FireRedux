package me.lazmaid.fireredux.model

/**
 * Created by VerachadW on 12/31/2016 AD.
 */

data class Note(var id: String = "note-${System.currentTimeMillis()}", var title: String = "", var content: String = "")