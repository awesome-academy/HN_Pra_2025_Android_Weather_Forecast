package com.sun.weatherapp.wind_detail

import android.location.Location
import com.sun.weatherapp.data.model.WeatherDetailResponse
import com.sun.weatherapp.data.reposiroty.LocationRepository
import com.sun.weatherapp.data.reposiroty.WeatherRepository
import com.sun.weatherapp.data.reposiroty.source.remote.OnResultListener
import com.sun.weatherapp.screen.wind_detail.WindDetailContract
import com.sun.weatherapp.screen.wind_detail.WindDetailPresenter
import com.sun.weatherapp.testutil.TestDummies
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.whenever

class WindDetailPresenterTest {
    private lateinit var presenter: WindDetailPresenter
    private val mockLocationRepo: LocationRepository = mock()
    private val mockWeatherRepo: WeatherRepository = mock()
    private val mockView: WindDetailContract.View = mock()

    @Before
    fun setup() {
        presenter = WindDetailPresenter(mockLocationRepo, mockWeatherRepo)
        presenter.attachView(mockView)
    }

    @Test
    fun `loadWindDetail should show loading and request location`() {
        presenter.loadWindDetail()

        verify(mockView).showLoading()
        verify(mockLocationRepo).getCurrentLocation(any())
    }

    @Test
    fun `refreshData should delegate to loadWindDetail`() {
        presenter.refreshData()
        verify(mockView).showLoading()
        verify(mockLocationRepo).getCurrentLocation(any())
    }

    @Test
    fun `location error should hide loading and show error`() {
        presenter.loadWindDetail()

        val captor = argumentCaptor<OnResultListener<Location>>()
        verify(mockLocationRepo).getCurrentLocation(captor.capture())
        val listener = captor.firstValue

        val error = Exception("loc error")
        listener.onError(error)

        verify(mockView).hideLoading()
        verify(mockView).showError("loc error")
    }

    @Test
    fun `weather success should hide loading and show wind data`() {
        presenter.loadWindDetail()

        val locationCaptor = argumentCaptor<OnResultListener<Location>>()
        verify(mockLocationRepo).getCurrentLocation(locationCaptor.capture())
        val locListener = locationCaptor.firstValue

        val fakeLocation = mock(Location::class.java)
        whenever(fakeLocation.latitude).thenReturn(1.0)
        whenever(fakeLocation.longitude).thenReturn(2.0)
        locListener.onSuccess(fakeLocation)

        val weatherCaptor = argumentCaptor<OnResultListener<WeatherDetailResponse>>()
        verify(mockWeatherRepo).getWeatherDetail(eq(1.0), eq(2.0), weatherCaptor.capture())
        val weatherListener = weatherCaptor.firstValue

        val data = TestDummies.stubWeatherDetail()
        weatherListener.onSuccess(data)

        verify(mockView).hideLoading()
        verify(mockView).showWindData(data)
    }

    @Test
    fun `weather error should hide loading and show error`() {
        presenter.loadWindDetail()

        val locationCaptor = argumentCaptor<OnResultListener<Location>>()
        verify(mockLocationRepo).getCurrentLocation(locationCaptor.capture())
        val locListener = locationCaptor.firstValue

        val fakeLocation = mock(Location::class.java)
        whenever(fakeLocation.latitude).thenReturn(1.0)
        whenever(fakeLocation.longitude).thenReturn(2.0)
        locListener.onSuccess(fakeLocation)

        val weatherCaptor = argumentCaptor<OnResultListener<WeatherDetailResponse>>()
        verify(mockWeatherRepo).getWeatherDetail(eq(1.0), eq(2.0), weatherCaptor.capture())
        val weatherListener = weatherCaptor.firstValue

        val error = Exception("wind error")
        weatherListener.onError(error)

        verify(mockView).hideLoading()
        verify(mockView).showError("wind error")
    }
}
