package me.lazmaid.cleanredux.data

import com.google.firebase.database.FirebaseDatabase
import me.lazmaid.cleanredux.extension.listChanged
import rx.Scheduler
import rx.Single
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by VerachadW on 12/31/2016 AD.
 */

class HomeRepositoryImpl(private val observerScheduler: Scheduler = AndroidSchedulers.mainThread()) : HomeRepository {

    override fun getNotes(): Single<List<Note>> {
        return FirebaseDatabase.getInstance().reference.listChanged<Note>()
                .subscribeOn(Schedulers.io()).observeOn(observerScheduler)
    }

}