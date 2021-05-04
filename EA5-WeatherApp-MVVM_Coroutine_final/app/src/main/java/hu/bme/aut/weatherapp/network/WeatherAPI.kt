package hu.bme.aut.weatherapp.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {
    @GET("/data/2.5/weather")
    suspend fun getWeatherData(@Query("q") cityName: String,
                       @Query("units") units: String,
                       @Query("appid") appId: String): WeatherResult
}