package me.lazmaid.cleanredux

import com.google.firebase.database.DatabaseError
import com.natpryce.hamkrest.Matcher
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.isA
import me.lazmaid.cleanredux.extension.FirebaseException
import me.lazmaid.cleanredux.extension.isFirebaseException
import me.lazmaid.cleanredux.model.Note
import me.lazmaid.cleanredux.presentation.HomeViewModel
import me.lazmaid.cleanredux.presentation.HomeViewModel.Action
import me.lazmaid.cleanredux.repository.HomeRepository
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import rx.Single
import rx.observers.TestSubscriber

/**
 * Created by VerachadW on 12/31/2016 AD.
 */

@RunWith(JUnitPlatform::class)
class HomeViewModelSpec : Spek({
    describe("HomeViewModel class") {
        val mockRepository = Mockito.mock(HomeRepository::class.java)
        var subscriber = TestSubscriber<HomeViewModel.State>()
        var viewModel = HomeViewModel(mockRepository)
        beforeEachTest {
            viewModel = HomeViewModel(mockRepository)
            subscriber = TestSubscriber<HomeViewModel.State>()
        }
        describe("Get Note Action") {
            val data = listOf(Note(title = "Note#1"), Note(title = "Note#2"))
            on("Success") {
                it("should mutate state with new note list") {
                    `when`(mockRepository.getNotes()).thenReturn(Single.just(data))
                    viewModel.stateChanged.subscribe(subscriber)
                    viewModel.dispatch(Action.GetNotesAction())
                    assertThat(subscriber.onNextEvents[0].items, equalTo(data))
                }
            }
            on("Fail") {
                it("should mutate state with error message") {
                    `when`(mockRepository.getNotes()).thenReturn(Single.error(FirebaseException(DatabaseError.UNKNOWN_ERROR, "test")))
                    viewModel.stateChanged.subscribe(subscriber)
                    viewModel.dispatch(Action.GetNotesAction())
                    assertThat(subscriber.onNextEvents[0].errorMessage, equalTo("test"))
                }
            }
        }
    }
})



