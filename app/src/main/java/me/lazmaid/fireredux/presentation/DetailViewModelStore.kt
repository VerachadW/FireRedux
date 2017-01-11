package me.lazmaid.fireredux.presentation

import me.lazmaid.fireredux.model.Note
import me.lazmaid.fireredux.navigation.StoreNavigator
import me.lazmaid.fireredux.navigation.ViewNavigator
import me.lazmaid.fireredux.repository.NoteRepository
import redux.api.Reducer
import redux.api.Store
import redux.api.enhancer.Middleware
import redux.applyMiddleware

/**
 * Created by VerachadW on 1/3/2017 AD.
 */

class DetailViewModelStore(private val navigator: ViewNavigator,
                           private val repository: NoteRepository) : ViewModelStore<DetailViewModelStore.State>(){

    data class State(
            val note: Note? = null
    )

    sealed class Action {
        class ShowNoteDetail(val note: Note) : Action()
        class UpdateNote(val id: String, val title: String, val content: String) : Action()
        class Back : Action()
    }

    val reducer = Reducer<State> { state, action ->
        when(action) {
            is Action.ShowNoteDetail -> state.copy(note = action.note)
            else -> state
        }
    }

//    val updateNoteEpic = Epic { actions, store ->
//        actions.ofType(Action.UpdateNote::class.java).flatMap {
//            repository.getNote(it.id).map {
//                Result.of(it)
//            }.onErrorReturn {
//                Result.error(it as Exception)
//            }
//        }.map { result ->
//            result.fold(success = {
//
//            })
//        }
//    }

    val navigationMiddleware = Middleware<DetailViewModelStore.State> { store, next, action ->
        when(action) {
            is Action.Back -> navigator.back()
        }
        next.dispatch(action)
    }

    override fun createStore(): Store<State> = redux.createStore(reducer = reducer,
            initialState = State(),
            enhancer = applyMiddleware(navigationMiddleware))

}

