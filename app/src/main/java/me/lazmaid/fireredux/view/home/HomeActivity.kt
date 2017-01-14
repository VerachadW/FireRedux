package me.lazmaid.fireredux.view.home

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.ViewGroup
import com.github.kittinunf.reactiveandroid.support.v7.widget.rx_itemsWith
import com.github.kittinunf.reactiveandroid.view.rx_click
import kotlinx.android.synthetic.main.activity_home.*
import me.lazmaid.fireredux.R
import me.lazmaid.fireredux.model.Note
import me.lazmaid.fireredux.navigation.StoreNavigator
import me.lazmaid.fireredux.presentation.HomeViewModelStore
import me.lazmaid.fireredux.presentation.HomeViewModelStore.Action
import me.lazmaid.fireredux.repository.NoteRepositoryImpl
import me.lazmaid.fireredux.view.BaseActivity
import rx.Observable

class HomeActivity : BaseActivity<HomeViewModelStore>() {
    override val viewModelStore: HomeViewModelStore by lazy {
        HomeViewModelStore(StoreNavigator(this), NoteRepositoryImpl())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(tbHome)
        rvNotes.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity)
        }

        fabCreate.rx_click().subscribe {
            viewModelStore.dispatch(Action.CreateNewNoteAction())
        }

        val notesObservable = viewModelStore.stateChanged.flatMap { Observable.just(it.items) }.bindUntilDestroyed()
        rvNotes.rx_itemsWith(notesObservable, onCreateViewHolder = { parent: ViewGroup?, viewType: Int ->
            val view = layoutInflater.inflate(R.layout.item_note, parent, false)
            HomeNoteItemViewHolder(view)
        }, onBindViewHolder = { holder: HomeNoteItemViewHolder, position: Int, item: Note ->
            holder.bindView(item)
        })

        viewModelStore.dispatch(Action.GetNotesAction())
    }

}
