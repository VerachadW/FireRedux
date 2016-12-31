package me.lazmaid.cleanredux.view

import android.support.v7.app.AppCompatActivity
import me.lazmaid.cleanredux.presentation.ViewModelStore
import rx.Observable
import rx.subjects.BehaviorSubject

/**
 * Created by VerachadW on 12/24/2016 AD.
 */

abstract class BaseActivity<out V: ViewModelStore<*>>: AppCompatActivity() {

    abstract val viewModel: V

    private val onDestroySubject = BehaviorSubject.create<Boolean>(false)

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        viewModel.stateChanged.publish()
    }

    override fun onDestroy() {
        super.onDestroy()
        onDestroySubject.onNext(true)
    }

    fun <T> Observable<T>.bindUntilDestroyed(): Observable<T> {
        return takeUntil(onDestroySubject.asObservable())
    }

}