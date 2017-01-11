package me.lazmaid.fireredux.repository

import me.lazmaid.fireredux.model.Note
import rx.Observable

/**
 * Created by VerachadW on 12/22/2016 AD.
 */

interface NoteRepository {
    fun getNotes(): Observable<List<Note>>
    fun getNote(id: String): Observable<Note>
}

