package me.lazmaid.cleanredux.presentation

import me.lazmaid.cleanredux.repository.HomeRepository
import me.lazmaid.cleanredux.model.Note
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
            val items: List<Note> = listOf()
    )

    sealed class Action {
        class ShowNotesAction(val notes: List<Note>): Action()
        class GetNotesAction: Action()
    }

    val reducer = Reducer<State> { state, action ->
        when(action) {
            is Action.ShowNotesAction -> state.copy(action.notes)
        }
        return@Reducer state
    }

    fun epic() = Epic { actions: Observable<out Any>, _: Store<State> ->
        actions.ofType(Action.GetNotesAction::class.java).flatMap {
            repository.getNotes().toObservable()
        }.map {
            Action.ShowNotesAction(notes = it)
        }
    }

    override fun createStore(): Store<State> = redux.createStore(
            reducer = reducer, initialState = State(),
            enhancer = applyMiddleware(createEpicMiddleware(epic()))
    )

}
