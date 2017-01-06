package me.lazmaid.fireredux.repository

import com.google.firebase.database.FirebaseDatabase
import me.lazmaid.fireredux.extension.listChangedOnce
import me.lazmaid.fireredux.extension.valueChanged
import me.lazmaid.fireredux.model.Note
import rx.Observable
import rx.Scheduler
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by VerachadW on 12/31/2016 AD.
 */

class NoteRepositoryImpl(private val observerScheduler: Scheduler = AndroidSchedulers.mainThread()) : NoteRepository {
    override fun getNote(id: String): Observable<Note> {
        return FirebaseDatabase.getInstance().reference.orderByChild("id").startAt(id).limitToFirst(1).ref.valueChanged()
    }

    override fun getNotes(): Observable<List<Note>> {
        return FirebaseDatabase.getInstance().reference.listChangedOnce<Note>()
                .subscribeOn(Schedulers.io()).observeOn(observerScheduler)
    }

}