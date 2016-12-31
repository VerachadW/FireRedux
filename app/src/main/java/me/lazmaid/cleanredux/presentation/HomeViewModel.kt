package me.lazmaid.cleanredux.presentation

import me.lazmaid.cleanredux.domain.HomeViewStore
import rx.subjects.PublishSubject

/**
 * Created by VerachadW on 12/26/2016 AD.
 */

class HomeViewModel : ViewModel<HomeViewStore.State, HomeViewStore>() {

    override val viewStore: HomeViewStore = HomeViewStore()

    data class NoteItem(val title: String)

    val itemChangedSubject: PublishSubject<List<NoteItem>> = PublishSubject.create<List<NoteItem>>()

    override fun onStateChanged(state: HomeViewStore.State) {
        itemChangedSubject.onNext(state.items.map {
            NoteItem(title = it.title)
        })
    }

}
