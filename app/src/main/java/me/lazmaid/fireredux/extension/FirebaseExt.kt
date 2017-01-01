package me.lazmaid.fireredux.extension

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import rx.Observable
import rx.Single

/**
 * Created by VerachadW on 12/31/2016 AD.
 */

class FirebaseException(val code: Int, message: String) : Exception(message)

fun <T> DatabaseReference.valueChangedOnce(): Observable<T> {
    return this.valueChanged<T>().take(1)
}

fun <T> DatabaseReference.listChangedOnce(): Observable<List<T>> {
    return this.listChanged<T>().take(1)
}

fun <T> DatabaseReference.valueChanged(): Observable<T> {
    return Observable.create<T> { subscriber ->
        addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                subscriber.onError(FirebaseException(error.code, error.message))
            }

            @Suppress("UNCHECKED_CAST")
            override fun onDataChange(data: DataSnapshot) {
                subscriber.onNext(data as T)
                subscriber.onCompleted()
            }
        })
    }
}

fun <T> DatabaseReference.listChanged(): Observable<List<T>> {
    return Observable.create<List<T>> { subscriber ->
        addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                subscriber.onError(FirebaseException(error.code, error.message))
            }

            @Suppress("UNCHECKED_CAST")
            override fun onDataChange(data: DataSnapshot) {
                subscriber.onNext(data.children as List<T>)
                subscriber.onCompleted()
            }
        })
    }
}
