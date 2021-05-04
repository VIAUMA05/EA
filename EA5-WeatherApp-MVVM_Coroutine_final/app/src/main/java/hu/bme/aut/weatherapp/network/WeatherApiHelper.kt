package hu.bme.aut.weatherapp.network

import hu.bme.aut.weatherapp.BuildConfig

class WeatherApiHelper(private val cityName: String, private val weatherApi: WeatherAPI) {

    suspend fun getWeather() = weatherApi.getWeatherData(
        cityName,
        "metric",
        BuildConfig.WEATHER_API_KEY
    )
}