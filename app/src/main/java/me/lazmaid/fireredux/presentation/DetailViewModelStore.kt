package me.lazmaid.fireredux.presentation

import me.lazmaid.fireredux.model.Note
import me.lazmaid.fireredux.navigation.ViewNavigator
import me.lazmaid.fireredux.repository.NoteRepository
import redux.api.Reducer
import redux.api.Store
import redux.applyMiddleware

/**
 * Created by VerachadW on 1/3/2017 AD.
 */

class DetailViewModelStore(private val navigator: ViewNavigator,
                           private val repository: NoteRepository) : ViewModelStore<DetailViewModelStore.State>(){

    data class State(
            val note: Note? = null
    )

    val reducer = Reducer<State> { state, action ->
        when(action) {
            else -> state
        }
    }

    override fun createStore(): Store<State> = redux.createStore(reducer = reducer,
            initialState = State(),
            enhancer = applyMiddleware())

}

