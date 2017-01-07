package me.lazmaid.fireredux

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.isNotNull
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import me.lazmaid.fireredux.model.Note
import me.lazmaid.fireredux.navigation.ViewNavigator
import me.lazmaid.fireredux.presentation.DetailViewModelStore
import me.lazmaid.fireredux.presentation.HomeViewModelStore
import me.lazmaid.fireredux.repository.NoteRepository
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

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