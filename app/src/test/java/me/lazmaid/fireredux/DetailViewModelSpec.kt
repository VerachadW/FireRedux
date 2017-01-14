package me.lazmaid.fireredux

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.isA
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import me.lazmaid.fireredux.model.Note
import me.lazmaid.fireredux.navigation.ViewNavigator
import me.lazmaid.fireredux.presentation.DetailViewModelStore
import me.lazmaid.fireredux.repository.NoteRepository
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import rx.Observable
import rx.observers.TestSubscriber

/**
 * Created by VerachadW on 1/7/2017 AD.
 */

@RunWith(JUnitPlatform::class)
class DetailViewModelSpec : Spek({
    describe("DetailViewModel class") {
        val mockNavigator = mock<ViewNavigator>()
        val mockRepository = mock<NoteRepository>()
        val viewModelStore = DetailViewModelStore(mockNavigator, mockRepository)
        describe("Reducer") {
            given("ShowNoteDetail is dispatched") {
                val fakeNote = Note()
                val action = DetailViewModelStore.Action.ShowNoteDetail(fakeNote)
                it("should mutate the state with new Note object") {
                    val newState = viewModelStore.reducer.reduce(viewModelStore.store.state, action)
                    assertThat(newState.note, equalTo(fakeNote))
                }
            }
            given("NoteCreated is dispatched") {
                val fakeNote = Note(title = "Test")
                val action = DetailViewModelStore.Action.NoteCreated(fakeNote)
                it("should mutate the state with exitMessage") {
                    val newState = viewModelStore.reducer.reduce(viewModelStore.store.state, action)
                    assertThat(newState.exitMessage, equalTo("Test is created!!"))
                }
            }
            given("ShowCreateError is dispatched") {
                val action = DetailViewModelStore.Action.ShowCreateError("<error-message>")
                it("should mutate the state with errorMessage") {
                    val newState = viewModelStore.reducer.reduce(viewModelStore.store.state, action)
                    assertThat(newState.errorMessage, equalTo("<error-message>"))
                }
            }
        }
        describe("CreateNoteEpic") {
           given("CreateNote is dispatched") {
               on("Success") {
                   it("should map into NoteCreated action") {
                       val subscriber = TestSubscriber<Any>()
                       whenever(mockRepository.createNote("<title>", "<content>")).thenReturn(Observable.just(Note("<title>", "<content>")))
                       val actionObservable = viewModelStore.createNoteEpic.map(Observable.just(DetailViewModelStore.Action.CreateNote("<title>", "<content>")), viewModelStore.store)
                       actionObservable.subscribe(subscriber)
                       assertThat(subscriber.onNextEvents[0] is DetailViewModelStore.Action.NoteCreated, equalTo(true))
                   }
               }
               on("Failure") {
                   it("should map into ShowCreateError action") {
                       val subscriber = TestSubscriber<Any>()
                       whenever(mockRepository.createNote("<title>", "<content>")).thenReturn(Observable.error(IllegalStateException()))
                       val actionObservable = viewModelStore.createNoteEpic.map(Observable.just(DetailViewModelStore.Action.CreateNote("<title>", "<content>")), viewModelStore.store)
                       actionObservable.subscribe(subscriber)
                       assertThat(subscriber.onNextEvents[0] is DetailViewModelStore.Action.ShowCreateError, equalTo(true))

                   }
               }
           }
        }
        describe("Navigation") {
           given("Back is dispatched") {
               it("should back to previous page") {
                   viewModelStore.dispatch(DetailViewModelStore.Action.Back())
                   verify(mockNavigator).back()
               }
           }
        }
    }
})