package me.lazmaid.fireredux.presentation

import com.github.kittinunf.result.Result
import me.lazmaid.fireredux.extension.FirebaseException
import me.lazmaid.fireredux.model.Note
import me.lazmaid.fireredux.repository.NoteRepository
import redux.api.Reducer
import redux.api.Store
import redux.applyMiddleware
import redux.observable.Epic
import redux.observable.createEpicMiddleware
import rx.Observable

/**
 * Created by VerachadW on 12/26/2016 AD.
 */

class HomeViewModelStore(private val repository: NoteRepository) : ViewModelStore<HomeViewModelStore.State>() {

    data class State(
            val items: List<Note> = listOf(),
            val errorMessage: String = "")

    sealed class Action {
        class ShowNotesAction(val notes: List<Note>): Action()
        class ShowErrorAction(val message: String): Action()
        class GetNotesAction: Action()
    }

    val reducer = Reducer<State> { state, action ->
        when(action) {
            is Action.ShowNotesAction -> state.copy(action.notes, errorMessage = "")
            is Action.ShowErrorAction -> state.copy(errorMessage = action.message)
            else -> state
        }
    }

    val getNotesEpic = Epic { actions: Observable<out Any>, store: Store<State> ->
        actions.ofType(Action.GetNotesAction::class.java).flatMap {
            repository.getNotes().map {
                Result.of(it)
            }.onErrorReturn {
                Result.error(it as Exception)
            }
        }.map { result ->
            result.fold(success = {
                Action.ShowNotesAction(it)
            }, failure = {
                Action.ShowErrorAction(it.message ?: "")
            })
        }
    }

    override fun createStore(): Store<State> = redux.createStore(
            reducer = reducer, initialState = State(),
            enhancer = applyMiddleware(createEpicMiddleware(getNotesEpic))
    )

    fun getNotes() {
        store.dispatch(Action.GetNotesAction())
    }

}
