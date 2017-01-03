package me.lazmaid.fireredux.presentation

import me.lazmaid.fireredux.model.Note
import me.lazmaid.fireredux.repository.NoteRepository
import redux.api.Store

/**
 * Created by VerachadW on 1/3/2017 AD.
 */

class DetailViewModelStore(private val repository: NoteRepository) : ViewModelStore<DetailViewModelStore.State>(){

    data class State(
            val note: Note
    )

    override fun createStore(): Store<State> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}

