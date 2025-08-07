package com.sun.weatherapp.screen.base

import java.lang.ref.WeakReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

abstract class BasePresenter<V> {

    private var viewRef: WeakReference<V>? = null

    protected val presenterScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    fun attachView(view: V) {
        viewRef = WeakReference(view)
    }

    fun detachView() {
        viewRef?.clear()
        viewRef = null

        presenterScope.cancel()
    }

    protected fun getView(): V? {
        return viewRef?.get()
    }

    protected fun isViewAttached(): Boolean {
        return getView() != null
    }
}
