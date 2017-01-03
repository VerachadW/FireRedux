package me.lazmaid.fireredux.navigation

import android.content.Context
import android.content.Intent

/**
 * Created by VerachadW on 1/3/2017 AD.
 */

interface ViewKey {
    fun createIntent(context: Context): Intent
}
