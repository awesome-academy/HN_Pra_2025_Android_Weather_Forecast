package com.sun.weatherapp.screen.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<VB : ViewBinding, P : BasePresenter<*>> : Fragment() {

    protected var _binding: VB? = null
    protected val binding get() = _binding!!
    protected var presenter: P? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getViewBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializePresenter()
        setupViews()
        setupListeners()
    }

    override fun onDestroyView() {
        // Automatically detach presenter to prevent memory leaks
        presenter?.detachView()
        presenter = null
        _binding = null
        super.onDestroyView()
    }

    abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB
    abstract fun initializePresenter()
    abstract fun setupViews()
    abstract fun setupListeners()
}
