package me.lazmaid.cleanredux.view.home

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.ViewGroup
import com.github.kittinunf.reactiveandroid.support.v7.widget.rx_itemsWith
import kotlinx.android.synthetic.main.activity_home.*
import me.lazmaid.cleanredux.R
import me.lazmaid.cleanredux.model.Note
import me.lazmaid.cleanredux.presentation.HomeViewModel
import me.lazmaid.cleanredux.presentation.HomeViewModel.Action
import me.lazmaid.cleanredux.repository.HomeRepositoryImpl
import me.lazmaid.cleanredux.view.BaseActivity
import rx.Observable

class HomeActivity : BaseActivity<HomeViewModel>() {
    override val viewModel: HomeViewModel by lazy {
        HomeViewModel(HomeRepositoryImpl())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(tbHome)
        rvNotes.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity)
        }
        viewModel.dispatch(Action.GetNotesAction())
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        val notesObservable = viewModel.stateChanged.flatMap { Observable.just(it.items) }.bindUntilDestroyed()
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
