package com.sun.weatherapp.rain_detail

import android.location.Location
import com.sun.weatherapp.data.model.CurrentWeather
import com.sun.weatherapp.data.model.DailyFeelsLike
import com.sun.weatherapp.data.model.DailyTemp
import com.sun.weatherapp.data.model.DailyWeather
import com.sun.weatherapp.data.model.HourlyWeather
import com.sun.weatherapp.data.model.Weather
import com.sun.weatherapp.data.model.WeatherDetailResponse
import com.sun.weatherapp.data.reposiroty.LocationRepository
import com.sun.weatherapp.data.reposiroty.WeatherRepository
import com.sun.weatherapp.data.reposiroty.source.remote.OnResultListener
import com.sun.weatherapp.screen.rain_detail.RainDetailContract
import com.sun.weatherapp.screen.rain_detail.RainDetailPresenter
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.whenever

class RainDetailPresenterTest {
    private lateinit var presenter: RainDetailPresenter
    private val mockLocationRepo: LocationRepository = mock()
    private val mockWeatherRepo: WeatherRepository = mock()
    private val mockView: RainDetailContract.View = mock()

    @Before
    fun setup() {
        presenter = RainDetailPresenter(mockLocationRepo, mockWeatherRepo)
        presenter.attachView(mockView)
    }

    @Test
    fun `loadRainDetail should show loading and request location`() {
        presenter.loadRainDetail()

        verify(mockView).showLoading()
        verify(mockLocationRepo).getCurrentLocation(any())
    }

    @Test
    fun `refreshData should delegate to loadRainDetail`() {
        presenter.refreshData()
        verify(mockView).showLoading()
        verify(mockLocationRepo).getCurrentLocation(any())
    }

    @Test
    fun `location error should hide loading and show error`() {
        presenter.loadRainDetail()

        val captor = argumentCaptor<OnResultListener<Location>>()
        verify(mockLocationRepo).getCurrentLocation(captor.capture())
        val listener = captor.firstValue

        val error = Exception("loc error")
        listener.onError(error)

        verify(mockView).hideLoading()
        verify(mockView).showError("loc error")
    }

    @Test
    fun `weather success should hide loading and show rain data`() {
        presenter.loadRainDetail()

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
        verify(mockView).showRainData(data)
    }

    @Test
    fun `weather error should hide loading and show error`() {
        presenter.loadRainDetail()

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

        val error = Exception("rain error")
        weatherListener.onError(error)

        verify(mockView).hideLoading()
        verify(mockView).showError("rain error")
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
