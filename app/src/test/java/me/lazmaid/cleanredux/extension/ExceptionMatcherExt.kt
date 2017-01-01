package me.lazmaid.cleanredux.extension

/**
 * Created by VerachadW on 12/31/2016 AD.
 */

fun Throwable.isFirebaseException(): Boolean = this is FirebaseException
