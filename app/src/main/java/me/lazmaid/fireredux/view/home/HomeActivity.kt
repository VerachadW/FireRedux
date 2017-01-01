package me.lazmaid.fireredux.view.home

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.ViewGroup
import com.github.kittinunf.reactiveandroid.support.v7.widget.rx_itemsWith
import kotlinx.android.synthetic.main.activity_home.*
import me.lazmaid.fireredux.R
import me.lazmaid.fireredux.model.Note
import me.lazmaid.fireredux.presentation.HomeViewModelStore
import me.lazmaid.fireredux.repository.NoteRepositoryImpl
import me.lazmaid.fireredux.view.BaseActivity
import rx.Observable

class HomeActivity : BaseActivity<HomeViewModelStore>() {
    override val viewModelStore: HomeViewModelStore by lazy {
        HomeViewModelStore(NoteRepositoryImpl())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(tbHome)
        rvNotes.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity)
        }
        viewModelStore.getNotes()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        val notesObservable = viewModelStore.stateChanged.flatMap { Observable.just(it.items) }.bindUntilDestroyed()
        rvNotes.rx_itemsWith(notesObservable, onCreateViewHolder = { parent: ViewGroup?, viewType: Int ->
            val view = layoutInflater.inflate(R.layout.item_note, parent, false)
            HomeNoteItemViewHolder(view)
        }, onBindViewHolder = { holder: HomeNoteItemViewHolder, position: Int, item: Note ->
            holder.bindView(item)
        })

    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
    }
}
