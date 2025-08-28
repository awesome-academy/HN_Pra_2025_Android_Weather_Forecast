package com.sun.weatherapp.home

import android.location.Location
import com.sun.weatherapp.data.reposiroty.LocationRepository
import com.sun.weatherapp.data.reposiroty.WeatherRepository
import com.sun.weatherapp.data.reposiroty.source.remote.OnResultListener
import com.sun.weatherapp.screen.home.HomeContract
import com.sun.weatherapp.screen.home.HomePresenter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class HomePresenterTest {
    private lateinit var presenter: HomePresenter
    private val mockLocationRepo: LocationRepository = mock()
    private val mockWeatherRepo: WeatherRepository = mock()
    private val mockView: HomeContract.View = mock()

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun init() {
        Dispatchers.setMain(testDispatcher)
        presenter = HomePresenter(mockLocationRepo, mockWeatherRepo)
        presenter.attachView(mockView)
    }

    @Test
    fun `loadCurrentWeather should show skeleton`() = runTest {
        presenter.loadCurrentWeather()
        testDispatcher.scheduler.advanceUntilIdle()
        verify(mockView).showSkeletonLoading()
    }

    @Test
    fun `refreshWeather should show skeleton and call api get current weather`() = runTest {
        presenter.loadCurrentWeather()
        testDispatcher.scheduler.advanceUntilIdle()

        verify(mockView).showSkeletonLoading()

        val captor = argumentCaptor<OnResultListener<Location>>()
        verify(mockLocationRepo).getCurrentLocation(captor.capture())
        val listener = captor.firstValue

        // Mock Location thay vì new
        val fakeLocation = mock(Location::class.java)
        whenever(fakeLocation.latitude).thenReturn(10.0)
        whenever(fakeLocation.longitude).thenReturn(20.0)

        listener.onSuccess(fakeLocation)

        verify(mockWeatherRepo).getCurrentWeather(
            eq(10.0),
            eq(20.0),
            any()
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
