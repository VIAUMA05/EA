package hu.bme.aut.weatherapp.viewmoel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import hu.bme.aut.weatherapp.network.RetrofitBuilder
import hu.bme.aut.weatherapp.network.WeatherApiHelper
import hu.bme.aut.weatherapp.repository.WeatherRepository
import hu.bme.aut.weatherapp.utils.Resource
import kotlinx.coroutines.Dispatchers

class WeatherViewModel() : ViewModel() {

    private val weatherRepository : WeatherRepository

    init {
        weatherRepository = WeatherRepository(WeatherApiHelper("Budapest", RetrofitBuilder.apiService))
    }

    fun getWeather() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = weatherRepository.getWeather()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}