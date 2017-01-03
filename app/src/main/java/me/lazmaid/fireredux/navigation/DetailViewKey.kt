package me.lazmaid.fireredux.navigation

import android.content.Context
import android.content.Intent
import me.lazmaid.fireredux.model.Note
import me.lazmaid.fireredux.view.detail.DetailActivity

/**
 * Created by VerachadW on 1/3/2017 AD.
 */

class DetailViewKey(selectedNote: Note) : ViewKey {

    override fun createIntent(context: Context): Intent = Intent(context, DetailActivity::class.java).apply {
    }

}