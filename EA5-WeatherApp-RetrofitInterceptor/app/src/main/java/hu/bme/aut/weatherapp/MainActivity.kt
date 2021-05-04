package hu.bme.aut.weatherapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import hu.bme.aut.weatherapp.databinding.ActivityMainBinding
import hu.bme.aut.weatherapp.network.WeatherAPI
import hu.bme.aut.weatherapp.network.WeatherResult
import okhttp3.ConnectionSpec
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.IOException
import java.util.*


class MainActivity : AppCompatActivity() {

    private  var cityName: String = "Budapest"

    lateinit var binding: ActivityMainBinding

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
        val weatherCall = prepareCall()

        weatherCall.enqueue(object : Callback<WeatherResult> {
            override fun onFailure(call: Call<WeatherResult>, t: Throwable) {
                binding.tvCity.text = t.message
            }

            override fun onResponse(call: Call<WeatherResult>, response: retrofit2.Response<WeatherResult>) {
                val weatherData = response.body()
                val icon = weatherData?.weather?.get(0)?.icon
                processResponse(weatherData, icon)
            }
        })
    }

    private fun processResponse(
            weatherData: WeatherResult?,
            icon: String?
    ) {
        Glide.with(this@MainActivity)
                .load("https://openweathermap.org/img/w/$icon.png")
                .into(binding.ivWeatherIcon)


        binding.tvMain.text = weatherData?.weather?.get(0)?.main
        binding.tvDescription.text = weatherData?.weather?.get(0)?.description
        binding.tvTemperature.text =
                getString(R.string.temperature, weatherData?.main?.temp?.toFloat().toString())
    }

    private fun prepareCall(): Call<WeatherResult> {
        //var client = OkHttpClient.Builder().connectionSpecs(
        //        Arrays.asList(ConnectionSpec.MODERN_TLS, ConnectionSpec.COMPATIBLE_TLS)).build()

        var client = OkHttpClient.Builder()
                .addInterceptor(LoggingInterceptor())
                .build()

        var retrofit = Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(MoshiConverterFactory.create())
                .client(client)
                .build()

        val weatherApi = retrofit.create(WeatherAPI::class.java)

        val weatherCall =
                weatherApi.getWeatherData(
                        cityName,
                        "metric",
                        BuildConfig.WEATHER_API_KEY
                )
        return weatherCall
    }

    inner class LoggingInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
            val request: Request = chain.request()
            val t1 = System.nanoTime()
            Log.d("TAG_HTTP", java.lang.String.format("Sending request %s on %s%n%s",
                    request.url(), chain.connection(), request.headers()))
            val response: okhttp3.Response = chain.proceed(request)
            val t2 = System.nanoTime()
            Log.d("TAG_HTTP", java.lang.String.format("Received response for %s in %.1fms%n%s",
                    response.request().url(), (t2 - t1) / 1e6, response.headers()))
            return response
        }
    }
}