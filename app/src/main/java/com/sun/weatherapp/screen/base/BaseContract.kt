package com.sun.weatherapp.screen.base

/**
 * Base contract interface for MVP pattern
 * @param V The type of view
 * @param P The type of presenter
 */
interface BaseContract<V, P> {

    interface View {
        fun showLoading()
        fun hideLoading()
        fun showError(message: String)
    }

    interface Presenter<V>
}
