package com.sun.weatherapp.screen.base

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.sun.weatherapp.utils.showProgressDialog

abstract class BaseFragment<VB : ViewBinding, P : BasePresenter<*>> : Fragment(), BaseContract.View {

    protected var _binding: VB? = null
    protected val binding get() = _binding!!
    protected var presenter: P? = null
    
    private var progressDialog: AlertDialog? = null

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
        presenter?.detachView()
        presenter = null
        
        progressDialog?.dismiss()
        progressDialog = null
        
        _binding = null
        super.onDestroyView()
    }

    override fun showLoading() {
        Log.d(this::class.simpleName, "Loading...")
        context?.let {
            if (progressDialog == null) {
                progressDialog = showProgressDialog()
            } else if (progressDialog?.isShowing == false) {
                progressDialog!!.show()
            }
        }
    }

    override fun hideLoading() {
        Log.d(this::class.simpleName, "Loading completed")
        progressDialog?.dismiss()
    }

    override fun showError(message: String) {
        Log.e(this::class.simpleName, "Error: $message")
        context?.let {
            Toast.makeText(it, message, Toast.LENGTH_LONG).show()
        }
    }

    abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB
    abstract fun initializePresenter()
    abstract fun setupViews()
    abstract fun setupListeners()
}
