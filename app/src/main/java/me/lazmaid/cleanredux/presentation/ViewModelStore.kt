package me.lazmaid.cleanredux.presentation

import redux.api.Store

/**
 * Created by VerachadW on 12/22/2016 AD.
 */

abstract class ViewModelStore<S>{

    private val store: Store<S> by lazy { createStore() }

    abstract fun createStore(): Store<S>

    fun dispatch(action: Any?) = store.dispatch(action)

}
