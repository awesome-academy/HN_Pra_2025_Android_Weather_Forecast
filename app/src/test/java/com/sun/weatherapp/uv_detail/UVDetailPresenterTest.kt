package com.sun.weatherapp.uv_detail

import android.location.Location
import com.sun.weatherapp.data.model.Clouds
import com.sun.weatherapp.data.model.CurrentWeather
import com.sun.weatherapp.data.model.DailyFeelsLike
import com.sun.weatherapp.data.model.DailyTemp
import com.sun.weatherapp.data.model.DailyWeather
import com.sun.weatherapp.data.model.HourlyWeather
import com.sun.weatherapp.data.model.Rain
import com.sun.weatherapp.data.model.Sys
import com.sun.weatherapp.data.model.Weather
import com.sun.weatherapp.data.model.WeatherDetailResponse
import com.sun.weatherapp.data.model.Wind
import com.sun.weatherapp.data.reposiroty.LocationRepository
import com.sun.weatherapp.data.reposiroty.WeatherRepository
import com.sun.weatherapp.data.reposiroty.source.remote.OnResultListener
import com.sun.weatherapp.screen.uv_detail.UVDetailContract
import com.sun.weatherapp.screen.uv_detail.UVDetailPresenter
import com.sun.weatherapp.testutil.TestDummies
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.whenever

class UVDetailPresenterTest {
    private lateinit var presenter: UVDetailPresenter
    private val mockLocationRepo: LocationRepository = mock()
    private val mockWeatherRepo: WeatherRepository = mock()
    private val mockView: UVDetailContract.View = mock()

    @Before
    fun setup() {
        presenter = UVDetailPresenter(mockLocationRepo, mockWeatherRepo)
        presenter.attachView(mockView)
    }

    @Test
    fun `funLoadUVDetail should show loading and request location`() {
        presenter.funLoadUVDetail()

        verify(mockView).showLoading()
        verify(mockLocationRepo).getCurrentLocation(any())
    }

    @Test
    fun `refreshData should delegate to funLoadUVDetail`() {
        presenter.refreshData()
        verify(mockView).showLoading()
        verify(mockLocationRepo).getCurrentLocation(any())
    }

    @Test
    fun `location error should hide loading and show error`() {
        presenter.funLoadUVDetail()

        val captor = argumentCaptor<OnResultListener<Location>>()
        verify(mockLocationRepo).getCurrentLocation(captor.capture())
        val listener = captor.firstValue

        val error = Exception("loc error")
        listener.onError(error)

        verify(mockView).hideLoading()
        verify(mockView).showError("loc error")
    }

    @Test
    fun `weather success should hide loading and show UV data`() {
        presenter.funLoadUVDetail()

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
        verify(mockView).showUVData(data)
    }

    @Test
    fun `weather error should hide loading and show error`() {
        presenter.funLoadUVDetail()

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

        val error = Exception("uv error")
        weatherListener.onError(error)

        verify(mockView).hideLoading()
        verify(mockView).showError("uv error")
    }
}
