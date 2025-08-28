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

        val data = stubWeatherDetail()
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

    private fun stubWeatherDetail(): WeatherDetailResponse {
        val weather = listOf(Weather(id = 800, main = "Clear", description = "clear", icon = "01d"))
        val current = CurrentWeather(
            dt = 0L,
            sunrise = 0L,
            sunset = 0L,
            temp = 300.0,
            feels_like = 300.0,
            pressure = 1013,
            humidity = 50,
            dew_point = 10.0,
            uvi = 5.0,
            clouds = 0,
            visibility = 10000,
            wind_speed = 1.0,
            wind_deg = 0,
            wind_gust = null,
            weather = weather
        )
        val hourly = listOf(
            HourlyWeather(
                dt = 0L,
                temp = 300.0,
                feels_like = 300.0,
                pressure = 1013,
                humidity = 50,
                dew_point = 10.0,
                uvi = 5.0,
                clouds = 0,
                visibility = 10000,
                wind_speed = 1.0,
                wind_deg = 0,
                wind_gust = null,
                weather = weather,
                pop = 0.0,
                rain = null
            )
        )
        val daily = listOf(
            DailyWeather(
                dt = 0L,
                sunrise = 0L,
                sunset = 0L,
                moonrise = null,
                moonset = null,
                moon_phase = 0.0,
                summary = "",
                temp = DailyTemp(300.0, 299.0, 301.0, 298.0, 300.0, 299.0),
                feels_like = DailyFeelsLike(300.0, 298.0, 299.0, 297.0),
                pressure = 1013,
                humidity = 50,
                dew_point = 10.0,
                wind_speed = 1.0,
                wind_deg = 0,
                wind_gust = null,
                weather = weather,
                clouds = 0,
                pop = 0.0,
                rain = null,
                uvi = 5.0
            )
        )
        return WeatherDetailResponse(
            lat = 0.0,
            lon = 0.0,
            timezone = "UTC",
            timezone_offset = 0,
            current = current,
            hourly = hourly,
            daily = daily
        )
    }
}
