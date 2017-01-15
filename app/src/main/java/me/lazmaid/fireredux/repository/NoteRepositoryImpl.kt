package me.lazmaid.fireredux.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import me.lazmaid.fireredux.extension.listChanged
import me.lazmaid.fireredux.extension.listChangedOnce
import me.lazmaid.fireredux.model.Note
import rx.Observable
import rx.Scheduler
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by VerachadW on 12/31/2016 AD.
 */

class NoteRepositoryImpl : NoteRepository {

    override fun getNotes(): Observable<List<Note>> {
        return FirebaseDatabase.getInstance().reference.child("notes").listChanged<Note>()
    }

    override fun createNote(title: String, content: String): Observable<String> {
        return Observable.create {
            val newNode = FirebaseDatabase.getInstance().reference.child("notes").push()
            val note = Note(title = title, content = content)
            newNode.updateChildren(note.toMap(), { databaseError, databaseReference ->
                if (databaseError != null) {
                    it.onError(databaseError.toException())
                } else {
                    it.onNext(note.title)
                    it.onCompleted()
                }
            })
        }
    }

    override fun updateNote(noteId: String, title: String, content: String): Observable<String> {
        return Observable.create { subscriber ->
            val ref = FirebaseDatabase.getInstance().reference
            val query = ref.child("notes").orderByChild("id").equalTo(noteId)
            query.addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onCancelled(error: DatabaseError?) {
                    subscriber.onError(error?.toException() ?: IllegalStateException())
                }

                override fun onDataChange(data: DataSnapshot?) {
                    if (data != null) {
                        val node = data.children.iterator().next()
                        val key = node.key
                        ref.child("notes/$key").updateChildren(mapOf("id" to noteId, "title" to title, "content" to "content"))
                        subscriber.onNext(title)
                    } else {
                        subscriber.onError(IllegalStateException())
                    }
                }

            })
        }
    }


}