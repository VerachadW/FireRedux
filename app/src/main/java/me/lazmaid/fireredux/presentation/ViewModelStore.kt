package me.lazmaid.fireredux.presentation

import redux.api.Store
import redux.asObservable
import rx.Observable

/**
 * Created by VerachadW on 12/26/2016 AD.
 */

abstract class ViewModelStore<S: Any> {

    val store: Store<S> by lazy { createStore() }
    val stateChanged: Observable<S> by lazy { store.asObservable().distinctUntilChanged()  }

    abstract fun createStore(): Store<S>

    fun dispatch(action: Any?) { store.dispatch(action) }

}