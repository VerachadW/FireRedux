package me.lazmaid.fireredux.presentation

import com.github.kittinunf.result.Result
import me.lazmaid.fireredux.model.Note
import me.lazmaid.fireredux.navigation.DetailViewKey
import me.lazmaid.fireredux.navigation.ViewNavigator
import me.lazmaid.fireredux.repository.NoteRepository
import redux.api.Reducer
import redux.api.Store
import redux.api.enhancer.Middleware
import redux.applyMiddleware
import redux.observable.Epic
import redux.observable.createEpicMiddleware
import rx.Observable

/**
 * Created by VerachadW on 12/26/2016 AD.
 */

class HomeViewModelStore(private val navigator: ViewNavigator,
                         private val repository: NoteRepository) : ViewModelStore<HomeViewModelStore.State>() {

    data class State(
            val items: List<Note> = listOf(),
            val errorMessage: String = "")

    sealed class Action {
        class ShowNotesAction(val notes: List<Note>) : Action()
        class ShowErrorAction(val message: String) : Action()
        class GetNotesAction : Action()
        class OpenNoteDetailAction(val note: Note) : Action()
        class CreateNewNoteAction : Action()
    }

    val reducer = Reducer<State> { state, action ->
        when (action) {
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

    val navigationMiddleware = Middleware<HomeViewModelStore.State> { store, next, action ->
        when (action) {
            is Action.OpenNoteDetailAction -> {
                navigator.navigateTo(DetailViewKey(action.note))
            }
            is Action.CreateNewNoteAction -> {
                navigator.navigateTo(DetailViewKey())
            }
        }
        next.dispatch(action)
    }

    override fun createStore(): Store<State> = redux.createStore(
            reducer = reducer, initialState = State(),
            enhancer = applyMiddleware(createEpicMiddleware(getNotesEpic),
                    navigationMiddleware)
    )

}
