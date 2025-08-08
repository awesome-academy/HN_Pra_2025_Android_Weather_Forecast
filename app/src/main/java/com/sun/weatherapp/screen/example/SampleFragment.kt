package com.sun.weatherapp.screen.example

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.sun.weatherapp.data.service.FirestoreService
import com.sun.weatherapp.databinding.FragmentSampleBinding
import com.sun.weatherapp.screen.base.BaseFragment

class SampleFragment : BaseFragment<FragmentSampleBinding, ExamplePresenter>(), ExampleContract.View {

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentSampleBinding {
        return FragmentSampleBinding.inflate(inflater, container, false)
    }

    override fun initializePresenter() {
        presenter = ExamplePresenter(TeamRepository())
    }

    override fun setupViews() {
        presenter?.attachView(this)
    }

    override fun setupListeners() {
        binding.loadMyTeamsButton.setOnClickListener {
            presenter?.loadMyTeams()
        }
    }

    override fun showMyTeams(teams: List<String>) {
        FirestoreService.db.collection("teams").document()
            .set(mapOf("teams" to teams))
            .addOnSuccessListener {
                Toast.makeText(context, "Teams saved successfully!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error saving teams: ${e.message}", Toast.LENGTH_LONG).show()
            }
//        Toast.makeText(
//            context,
//            teams.joinToString(", "),
//            Toast.LENGTH_LONG
//        ).show()
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
}
