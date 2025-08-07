package com.sun.weatherapp.screen.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB : ViewBinding, P : BasePresenter<*>> : AppCompatActivity() {

    protected lateinit var binding: VB
    protected var presenter: P? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewBinding()
        setContentView(binding.root)

        initializePresenter()
        setupViews()
        setupListeners()
    }

    override fun onDestroy() {
        presenter?.detachView()
        presenter = null
        super.onDestroy()
    }

    abstract fun getViewBinding(): VB
    abstract fun initializePresenter()
    abstract fun setupViews()
    abstract fun setupListeners()
}
