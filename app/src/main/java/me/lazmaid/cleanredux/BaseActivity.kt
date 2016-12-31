package me.lazmaid.cleanredux

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import me.lazmaid.cleanredux.presentation.ViewModel

/**
 * Created by VerachadW on 12/24/2016 AD.
 */

abstract class BaseActivity<out V: ViewModel<*, *>> : AppCompatActivity() {

    abstract val view: V

    override fun onStart() {
        super.onStart()
        view.onAttached()
    }

    override fun onStop() {
        view.onDetached()
        super.onStop()
    }

}