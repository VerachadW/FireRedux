package me.lazmaid.cleanredux.presentation

import me.lazmaid.cleanredux.domain.ViewModelStore
import rx.Subscription

/**
 * Created by VerachadW on 12/26/2016 AD.
 */

abstract class ViewModel<S: Any, out T : ViewModelStore<S>> {

    abstract val viewStore: T
    abstract fun onStateChanged(state: S)

    lateinit var storeSubscription: Subscription

    fun onAttached() {
        storeSubscription = viewStore.subscribe{ onStateChanged(it) }
    }

    fun onDetached() {
        storeSubscription.unsubscribe()
    }
}