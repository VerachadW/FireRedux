package me.lazmaid.cleanredux.domain

import redux.api.Store
import redux.asObservable
import rx.Subscription

/**
 * Created by VerachadW on 12/22/2016 AD.
 */

abstract class ViewModelStore<S : Any> {

    private val store: Store<S> by lazy { createStore() }

    protected abstract fun createStore(): Store<S>

    fun dispatch(action: Any?) { store.dispatch(action) }

    fun subscribe(stateChanged: (S) -> Unit) : Subscription = store.asObservable().distinctUntilChanged().subscribe {
        stateChanged(it)
    }
}
