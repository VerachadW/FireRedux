package me.lazmaid.fireredux.navigation

import android.content.Context
import android.content.Intent
import me.lazmaid.fireredux.view.home.HomeActivity

/**
 * Created by VerachadW on 1/3/2017 AD.
 */

class HomeViewKey : ViewKey {

    override fun createIntent(context: Context): Intent = Intent(context, HomeActivity::class.java)

}