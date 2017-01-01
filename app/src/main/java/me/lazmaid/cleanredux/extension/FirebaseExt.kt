package me.lazmaid.cleanredux.extension

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import rx.Single

/**
 * Created by VerachadW on 12/31/2016 AD.
 */

class FirebaseException(val code: Int, message: String) : Exception(message)

fun <T> DatabaseReference.valueChanged(): Single<T> {
    return Single.create<T> { subscriber ->
        addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                subscriber.onError(FirebaseException(error.code, error.message))
            }

            @Suppress("UNCHECKED_CAST")
            override fun onDataChange(data: DataSnapshot) {
                subscriber.onSuccess(data as T)
            }
        })
    }
}

fun <T> DatabaseReference.listChanged(): Single<List<T>> {
    return Single.create<List<T>> { subscriber ->
        addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                subscriber.onError(FirebaseException(error.code, error.message))
            }

            @Suppress("UNCHECKED_CAST")
            override fun onDataChange(data: DataSnapshot) {
                subscriber.onSuccess(data.children as List<T>)
            }
        })
    }
}
