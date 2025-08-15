package com.sun.weatherapp.screen.wind_detail

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.sun.weatherapp.databinding.FragmentWindDetailBinding
import com.sun.weatherapp.screen.base.BaseFragment
import com.sun.weatherapp.screen.widget.ChartView
import kotlin.random.Random


class WindDetailFragment : BaseFragment<FragmentWindDetailBinding, WindDetailPresenter>(), WindDetailContract.View {
    
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentWindDetailBinding {
        return FragmentWindDetailBinding.inflate(
            layoutInflater,
            container,
            false
        )
    }

    override fun initializePresenter() {
        presenter = WindDetailPresenter()
        presenter?.attachView(this)
    }

    override fun setupViews() {
        binding.apply {
            icBack.setOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
        val hours = (0..23).map { it.toFloat() }
        val temperatures = (0..23).map { Random.nextInt(-10, 35).toFloat() }

        binding.weatherChart.let { chart ->
            chart.setData(hours, temperatures, maximumNumberOfDisplayPointInXAxis = 12, title = "Dự báo theo giờ", highlightIndex = 3)
            chart.setOnPointSelectedListener(object : ChartView.OnPointSelectedListener {
                override fun onPointSelected(xValue: Float, yValue: Float, index: Int) {
                    Toast.makeText(
                        context,
                        "Selected: ${xValue.toInt()}h - ${yValue.toInt()}°C",
                        Toast.LENGTH_SHORT
                    ).show()

                    Log.d("ChartView", "Point selected: x=$xValue, y=$yValue, index=$index")
                }
                override fun onPointDeselected() {
                    Log.d("ChartView", "Point deselected")
                }
            })
        }
    }

    override fun setupListeners() {

    }




}