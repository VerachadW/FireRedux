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
        class CreateNote(val title: String, val content: String) : Action()
        class NoteCreated(val note: Note) : Action()
        class ShowCreateError(val errorMessage: String) : Action()
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
                val message = "${action.note.title} is created!!"
                state.copy(exitMessage = message)
            }
            is Action.ShowCreateError -> state.copy(errorMessage = action.errorMessage)
            else -> state
        }
    }

    val createNoteEpic = Epic<State> { actions, store ->
        actions.ofType(Action.CreateNote::class.java).flatMap {
            repository.createNote(it.title, it.content).map {
                Result.of(it)
            }.onErrorReturn {
                Result.error(it as Exception)
            }
        }.map { result ->
            result.fold(success = {
                Action.NoteCreated(it)
            }, failure = {
                Action.ShowCreateError(it.message ?: "")
            })
        }
    }

    val navigationMiddleware = Middleware<State> { store, next, action ->
        when(action) {
            is Action.Back -> navigator.back()
        }
        next.dispatch(action)
    }

    override fun createStore(): Store<State> = redux.createStore(reducer = reducer,
            initialState = State(),
            enhancer = applyMiddleware(navigationMiddleware, createEpicMiddleware(createNoteEpic)))

}

