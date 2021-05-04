package hu.bme.aut.weatherapp.repository

import hu.bme.aut.weatherapp.network.WeatherApiHelper

class WeatherRepository(private val apiHelper: WeatherApiHelper) {
    suspend fun getWeather() = apiHelper.getWeather()
}