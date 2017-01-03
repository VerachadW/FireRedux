package me.lazmaid.fireredux.view.detail

import android.os.Bundle
import me.lazmaid.fireredux.R
import me.lazmaid.fireredux.presentation.DetailViewModelStore
import me.lazmaid.fireredux.repository.NoteRepositoryImpl
import me.lazmaid.fireredux.view.BaseActivity

class DetailActivity : BaseActivity<DetailViewModelStore>() {

    override val viewModelStore: DetailViewModelStore by lazy {
        DetailViewModelStore(NoteRepositoryImpl())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
    }
}
