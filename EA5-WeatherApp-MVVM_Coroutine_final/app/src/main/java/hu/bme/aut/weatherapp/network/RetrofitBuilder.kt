package hu.bme.aut.weatherapp.network

import android.util.Log
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.IOException

object RetrofitBuilder {

    private const val BASE_URL = "https://api.openweathermap.org/"

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(OkHttpClient.Builder()
                .addInterceptor(LoggingInterceptor())
                .build())
            .build() //Doesn't require the adapter
    }

    val apiService: WeatherAPI = getRetrofit().create(WeatherAPI::class.java)
}


