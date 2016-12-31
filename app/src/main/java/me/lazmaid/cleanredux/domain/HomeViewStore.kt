package me.lazmaid.cleanredux.domain

import me.lazmaid.cleanredux.data.Note
import redux.api.Reducer
import redux.api.Store


/**
 * Created by VerachadW on 12/24/2016 AD.
 */

class HomeViewStore : ViewModelStore<HomeViewStore.State>() {

    data class State(
            val items: List<Note> = listOf()
    )

    val reducer = Reducer<State> { state, action ->
        return@Reducer state
    }

    override fun createStore(): Store<State> = redux.createStore(
            reducer = reducer, initialState = State()
    )

}