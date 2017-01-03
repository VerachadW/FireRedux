package me.lazmaid.fireredux.navigation

import android.app.Activity
import java.lang.ref.WeakReference

/**
 * Created by VerachadW on 1/3/2017 AD.
 */

class ViewNavigator(ctx: Activity): ViewNavigatorService {
    val context = WeakReference<Activity>(ctx)

    override fun navigateTo(viewKey: ViewKey) {
        context.get()?.let {
            it.startActivity(viewKey.createIntent(it))
        }
    }

    override fun back() {
        context.get()?.finish()
    }
}
