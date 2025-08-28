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
import com.sun.weatherapp.data.model.Clouds
import com.sun.weatherapp.data.model.Coord
import com.sun.weatherapp.data.model.Main
import com.sun.weatherapp.data.model.Sys
import com.sun.weatherapp.data.model.Weather
import com.sun.weatherapp.data.model.WeatherResponse
import com.sun.weatherapp.data.model.Wind

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

    @Test
    fun `refreshWeather should set refreshing true and show skeleton`() = runTest {
        presenter.refreshWeather()

        verify(mockView).setRefreshing(true)
        verify(mockView).showSkeletonLoading()
    }

    @Test
    fun `location error should hide loading, stop refreshing and show error`() = runTest {
        presenter.loadCurrentWeather()
        testDispatcher.scheduler.advanceUntilIdle()

        val locationCaptor = argumentCaptor<OnResultListener<Location>>()
        verify(mockLocationRepo).getCurrentLocation(locationCaptor.capture())
        val locationListener = locationCaptor.firstValue

        val exception = Exception("location failed")
        locationListener.onError(exception)

        verify(mockView).hideSkeletonLoading()
        verify(mockView).setRefreshing(false)
        verify(mockView).showError("location failed")
    }

    @Test
    fun `weather success should hide loading, stop refreshing and show current weather`() = runTest {
        presenter.loadCurrentWeather()
        testDispatcher.scheduler.advanceUntilIdle()

        val locationCaptor = argumentCaptor<OnResultListener<Location>>()
        verify(mockLocationRepo).getCurrentLocation(locationCaptor.capture())
        val locationListener = locationCaptor.firstValue

        val fakeLocation = mock(Location::class.java)
        whenever(fakeLocation.latitude).thenReturn(10.0)
        whenever(fakeLocation.longitude).thenReturn(20.0)

        locationListener.onSuccess(fakeLocation)

        val weatherCaptor = argumentCaptor<OnResultListener<WeatherResponse>>()
        verify(mockWeatherRepo).getCurrentWeather(eq(10.0), eq(20.0), weatherCaptor.capture())
        val weatherListener = weatherCaptor.firstValue

        val successData = stubWeatherResponse()
        weatherListener.onSuccess(successData)

        verify(mockView).hideSkeletonLoading()
        verify(mockView).setRefreshing(false)
        verify(mockView).showCurrentWeather(successData)
    }

    @Test
    fun `weather error should hide loading, stop refreshing and show error`() = runTest {
        presenter.loadCurrentWeather()
        testDispatcher.scheduler.advanceUntilIdle()

        val locationCaptor = argumentCaptor<OnResultListener<Location>>()
        verify(mockLocationRepo).getCurrentLocation(locationCaptor.capture())
        val locationListener = locationCaptor.firstValue

        val fakeLocation = mock(Location::class.java)
        whenever(fakeLocation.latitude).thenReturn(10.0)
        whenever(fakeLocation.longitude).thenReturn(20.0)

        locationListener.onSuccess(fakeLocation)

        val weatherCaptor = argumentCaptor<OnResultListener<WeatherResponse>>()
        verify(mockWeatherRepo).getCurrentWeather(eq(10.0), eq(20.0), weatherCaptor.capture())
        val weatherListener = weatherCaptor.firstValue

        val exception = Exception("weather failed")
        weatherListener.onError(exception)

        verify(mockView).hideSkeletonLoading()
        verify(mockView).setRefreshing(false)
        verify(mockView).showError("weather failed")
    }

    private fun stubWeatherResponse(): WeatherResponse {
        return WeatherResponse(
            coord = Coord(lon = 0.0, lat = 0.0),
            weather = listOf(Weather(id = 800, main = "Clear", description = "clear sky", icon = "01d")),
            base = "stations",
            main = Main(
                temp = 300.0,
                feels_like = 300.0,
                temp_min = 299.0,
                temp_max = 301.0,
                pressure = 1013,
                humidity = 50,
                sea_level = null,
                grnd_level = null
            ),
            visibility = 10000,
            wind = Wind(speed = 1.5, deg = 350, gust = null),
            rain = null,
            clouds = Clouds(all = 0),
            dt = 0L,
            sys = Sys(type = null, id = null, country = "US", sunrise = 0L, sunset = 0L),
            timezone = 0,
            id = 0L,
            name = "Test City",
            cod = 200
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
