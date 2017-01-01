package me.lazmaid.fireredux.extension

import me.lazmaid.fireredux.extension.FirebaseException

/**
 * Created by VerachadW on 12/31/2016 AD.
 */

fun Throwable.isFirebaseException(): Boolean = this is FirebaseException
