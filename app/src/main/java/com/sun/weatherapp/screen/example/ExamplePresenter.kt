package com.sun.weatherapp.screen.example

import com.sun.weatherapp.screen.base.BasePresenter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExamplePresenter(
    private val teamRepository: ITeamRepository
) : BasePresenter<ExampleContract.View>(), ExampleContract.Presenter {

    override fun loadMyTeams() {
        presenterScope.launch {
            val teams = try {
                withContext(Dispatchers.IO) {
                    teamRepository.getMyTeams()
                }
            } catch (e: Exception) {
                getView()?.showError("Failed to load teams: ${e.message}")
                return@launch
            }
            if (isViewAttached()) {
                getView()?.showMyTeams(teams)
            }
        }
    }
}
