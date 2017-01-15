package me.lazmaid.fireredux.repository

import me.lazmaid.fireredux.model.Note
import rx.Observable

/**
 * Created by VerachadW on 12/22/2016 AD.
 */

interface NoteRepository {
    fun getNotes(): Observable<List<Note>>
    fun createNote(title: String, content: String): Observable<String>
    fun updateNote(noteId: String, title: String, content: String): Observable<String>
}

