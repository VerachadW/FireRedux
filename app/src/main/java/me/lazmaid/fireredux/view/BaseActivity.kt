package me.lazmaid.fireredux.view

import android.support.v7.app.AppCompatActivity
import me.lazmaid.fireredux.presentation.ViewModelStore
import rx.Observable
import rx.Subscription
import rx.subjects.BehaviorSubject

/**
 * Created by VerachadW on 12/24/2016 AD.
 */

abstract class BaseActivity<out V: ViewModelStore<*>>: AppCompatActivity() {

    abstract val viewModelStore: V

    private val onDestroySubject = BehaviorSubject.create<Boolean>(false)
    private lateinit var stateChangedSubscription: Subscription

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        viewModelStore.stateChanged.publish()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
    }

    override fun onDestroy() {
        super.onDestroy()
        onDestroySubject.onNext(true)
    }

    fun <T> Observable<T>.bindUntilDestroyed(): Observable<T> {
        return takeUntil(onDestroySubject.asObservable())
    }

}