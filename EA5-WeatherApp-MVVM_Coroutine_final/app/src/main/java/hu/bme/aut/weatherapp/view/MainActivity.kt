package hu.bme.aut.weatherapp.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import hu.bme.aut.weatherapp.R
import hu.bme.aut.weatherapp.databinding.ActivityMainBinding
import hu.bme.aut.weatherapp.network.WeatherResult
import hu.bme.aut.weatherapp.utils.Status
import hu.bme.aut.weatherapp.viewmoel.WeatherViewModel

class MainActivity : AppCompatActivity() {

    private  var cityName: String = "Budapest"

    lateinit var binding: ActivityMainBinding

    private val weatherViewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvCity.text = cityName
    }

    override fun onResume() {
        super.onResume()

        getWeatherData()
    }

    private fun getWeatherData() {
        weatherViewModel.getWeather().observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { weatherData -> processResponse(weatherData) }
                    }
                    Status.ERROR -> {
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        Toast.makeText(this, "Loading", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }

    private fun processResponse(
            weatherData: WeatherResult?
    ) {
        val icon = weatherData?.weather?.get(0)?.icon
        Glide.with(this@MainActivity)
                .load("https://openweathermap.org/img/w/$icon.png")
                .into(binding.ivWeatherIcon)


        binding.tvMain.text = weatherData?.weather?.get(0)?.main
        binding.tvDescription.text = weatherData?.weather?.get(0)?.description
        binding.tvTemperature.text =
                getString(R.string.temperature, weatherData?.main?.temp?.toFloat().toString())
    }


}