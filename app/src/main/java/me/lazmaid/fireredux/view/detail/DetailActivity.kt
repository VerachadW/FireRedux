package me.lazmaid.fireredux.view.detail

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.github.kittinunf.reactiveandroid.widget.rx_text
import kotlinx.android.synthetic.main.activity_detail.*
import me.lazmaid.fireredux.R
import me.lazmaid.fireredux.model.Note
import me.lazmaid.fireredux.navigation.DetailViewKey
import me.lazmaid.fireredux.navigation.StoreNavigator
import me.lazmaid.fireredux.presentation.DetailViewModelStore
import me.lazmaid.fireredux.repository.NoteRepositoryImpl
import me.lazmaid.fireredux.view.BaseActivity

class DetailActivity : BaseActivity<DetailViewModelStore>() {

    override val viewModelStore: DetailViewModelStore by lazy {
        DetailViewModelStore(StoreNavigator(this), NoteRepositoryImpl())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        setSupportActionBar(tbDetail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val note = intent.getParcelableExtra<Note>(DetailViewKey.KEY_SELECTED_NOTE)
        viewModelStore.dispatch(DetailViewModelStore.Action.ShowNoteDetail(note))

        val stateObservable = viewModelStore.stateChanged.share()

        val noteObservable = stateObservable.filter { it.note != null }.map { it.note!! }
        etTitle.rx_text.bindTo(noteObservable.map(Note::title)
                .cast(CharSequence::class.java).bindUntilDestroyed())
        etContent.rx_text.bindTo(noteObservable.map(Note::content)
                .cast(CharSequence::class.java).bindUntilDestroyed())

        stateObservable.map { it.exitMessage }.filter(String::isNotBlank)
                .bindUntilDestroyed()
                .doOnNext {
                    setResult(RESULT_OK)
                    viewModelStore.dispatch(DetailViewModelStore.Action.Back())
                }.subscribe {
                    Toast.makeText(this@DetailActivity, it, Toast.LENGTH_SHORT).show()
                }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                viewModelStore.dispatch(DetailViewModelStore.Action.Back())
            }
            R.id.miDone -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }

}

