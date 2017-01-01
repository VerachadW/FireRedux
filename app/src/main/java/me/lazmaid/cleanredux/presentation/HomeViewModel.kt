package me.lazmaid.cleanredux.presentation

import com.github.kittinunf.result.Result
import me.lazmaid.cleanredux.extension.FirebaseException
import me.lazmaid.cleanredux.model.Note
import me.lazmaid.cleanredux.repository.HomeRepository
import redux.api.Reducer
import redux.api.Store
import redux.applyMiddleware
import redux.observable.Epic
import redux.observable.createEpicMiddleware
import rx.Observable

/**
 * Created by VerachadW on 12/26/2016 AD.
 */

class HomeViewModel(private val repository: HomeRepository) : ViewModelStore<HomeViewModel.State>() {

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

    fun epic() = Epic { actions: Observable<out Any>, store: Store<State> ->
        actions.ofType(Action.GetNotesAction::class.java).flatMap {
            repository.getNotes().map {
                Result.of(it)
            }.onErrorReturn {
                Result.error(it as Exception)
            }.toObservable()
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
            enhancer = applyMiddleware(createEpicMiddleware(epic()))
    )

}
