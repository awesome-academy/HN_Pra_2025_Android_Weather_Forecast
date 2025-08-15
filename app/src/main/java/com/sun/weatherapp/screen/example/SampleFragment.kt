package com.sun.weatherapp.screen.example

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.sun.weatherapp.data.reposiroty.FirebaseRepository
import com.sun.weatherapp.data.service.FirestoreService
import com.sun.weatherapp.databinding.FragmentSampleBinding
import com.sun.weatherapp.screen.base.BaseFragment
import com.sun.weatherapp.screen.widget.ChartView
import kotlin.random.Random

class SampleFragment : BaseFragment<FragmentSampleBinding, ExamplePresenter>(), ExampleContract.View {

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentSampleBinding {
        return FragmentSampleBinding.inflate(inflater, container, false)
    }

    override fun initializePresenter() {
        presenter = ExamplePresenter(TeamRepository(), FirebaseRepository())
    }

    override fun setupViews() {
        presenter?.attachView(this)
        setupDemoChart()
    }

    override fun setupListeners() {
        binding.loadMyTeamsButton.setOnClickListener {
            presenter?.loadMyTeams()
        }
    }

    override fun showMyTeams(teams: List<String>) {
        presenter?.saveMyTeams(teams)
    }

    override fun showLoading() {
        Toast.makeText(
            context,
            "Loading teams...",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun hideLoading() {
    }

    override fun showError(message: String) {
        Toast.makeText(
            context,
            "Error: $message",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun setupDemoChart() {
        val hours = (0..23).map { it.toFloat() }
        val temperatures = (0..23).map { Random.nextInt(-10, 35).toFloat() }

        binding.weatherChart.let { chart ->
            chart.setData(hours, temperatures, maximumNumberOfDisplayPointInXAxis = 12, title = "Biểu đồ nhiệt độ", highlightIndex = 3)
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
    
    private fun updateChartsWithRandomData() {

    }
}
