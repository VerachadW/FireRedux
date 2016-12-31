package me.lazmaid.cleanredux.data

import rx.Single

/**
 * Created by VerachadW on 12/22/2016 AD.
 */

interface HomeRepository {
    fun getNotes(): Single<List<Note>>
}

