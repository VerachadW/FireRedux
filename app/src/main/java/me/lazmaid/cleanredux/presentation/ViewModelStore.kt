package me.lazmaid.cleanredux.presentation

import redux.api.Store
import redux.asObservable
import rx.Observable

/**
 * Created by VerachadW on 12/26/2016 AD.
 */

abstract class ViewModelStore<S: Any> {

    private val store: Store<S> by lazy { createStore() }
    val stateChanged: Observable<S> = store.asObservable().distinctUntilChanged().share()

    protected abstract fun createStore(): Store<S>

    fun dispatch(action: Any?) { store.dispatch(action) }

}