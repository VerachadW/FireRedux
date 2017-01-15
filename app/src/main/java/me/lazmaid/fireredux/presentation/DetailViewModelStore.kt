package me.lazmaid.fireredux.presentation

import com.github.kittinunf.result.Result
import me.lazmaid.fireredux.model.Note
import me.lazmaid.fireredux.navigation.StoreNavigator
import me.lazmaid.fireredux.navigation.ViewNavigator
import me.lazmaid.fireredux.repository.NoteRepository
import redux.api.Reducer
import redux.api.Store
import redux.api.enhancer.Middleware
import redux.applyMiddleware
import redux.observable.Epic
import redux.observable.combineEpics
import redux.observable.createEpicMiddleware

/**
 * Created by VerachadW on 1/3/2017 AD.
 */

class DetailViewModelStore(private val navigator: ViewNavigator,
                           private val repository: NoteRepository) : ViewModelStore<DetailViewModelStore.State>(){

    enum class Mode {
        CREATE,
        UPDATE
    }

    data class State(
            val note: Note? = null,
            val mode: Mode = Mode.CREATE,
            val exitMessage: String = "",
            val errorMessage: String = ""
    )

    sealed class Action {
        class ShowNoteDetail(val note: Note?) : Action()
        class CreateOrUpdateNote(val title: String, val content: String) : Action()
        class NoteUpdated(val noteTitle: String) : Action()
        class NoteCreated(val noteTitle: String) : Action()
        class ShowError(val errorMessage: String) : Action()
        class Back : Action()
    }

    val reducer = Reducer<State> { state, action ->
        when(action) {
            is Action.ShowNoteDetail ->  {
                if (action.note != null) {
                    state.copy(mode = Mode.UPDATE, note = action.note)
                } else {
                    state.copy(mode = Mode.CREATE, note = Note())
                }
            }
            is Action.NoteCreated -> {
                val message = "${action.noteTitle} is created!!"
                state.copy(exitMessage = message)
            }
            is Action.NoteUpdated -> {
                val message =  "${action.noteTitle} is updated!!"
                state.copy(exitMessage = message)
            }
            is Action.ShowError -> state.copy(errorMessage = action.errorMessage)
            else -> state
        }
    }

    val createNoteEpic = Epic<State> { actions, store ->
        actions.ofType(Action.CreateOrUpdateNote::class.java)
        .filter { store.state.mode == Mode.CREATE }
        .flatMap {
            repository.createNote(it.title, it.content).map {
                Result.of(it)
            }.onErrorReturn {
                Result.error(it as Exception)
            }
        }.map { result ->
            result.fold(success = {
                Action.NoteCreated(it)
            }, failure = {
                Action.ShowError(it.message ?: "")
            })
        }
    }

    val updateNoteEpic = Epic<State> { actions, store ->
        actions.ofType(Action.CreateOrUpdateNote::class.java)
        .filter{ store.state.mode == Mode.UPDATE }
        .flatMap {
            repository.updateNote(store.state.note!!.id, it.title, it.content).map {
                Result.of(it)
            }.onErrorReturn {
                Result.error(it as Exception)
            }
        }.map { result ->
            result.fold(success = {
                Action.NoteUpdated(it)
            }, failure = {
                Action.ShowError(it.message ?: "")
            })
        }
    }

    val navigationMiddleware = Middleware<State> { store, next, action ->
        when(action) {
            is Action.Back, is Action.NoteCreated, is Action.NoteUpdated -> navigator.back()
        }
        next.dispatch(action)
    }

    override fun createStore(): Store<State> = redux.createStore(reducer = reducer,
            initialState = State(),
            enhancer = applyMiddleware(navigationMiddleware, createEpicMiddleware(combineEpics(createNoteEpic, updateNoteEpic))))

}

