package me.lazmaid.fireredux.navigation

import android.content.Context
import android.content.Intent
import me.lazmaid.fireredux.view.detail.DetailActivity

/**
 * Created by VerachadW on 1/3/2017 AD.
 */

class DetailViewKey(val selectedNoteId: String) : ViewKey {

    override fun createIntent(context: Context): Intent = Intent(context, DetailActivity::class.java).apply {
        putExtra("selectedId", selectedNoteId)
    }

}