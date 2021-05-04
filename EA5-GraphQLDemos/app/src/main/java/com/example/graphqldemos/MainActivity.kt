package com.example.graphqldemos

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.ApolloQueryCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.example.RocketsQuery
import okhttp3.OkHttpClient
import java.util.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        findViewById<Button>(R.id.btnDemo).setOnClickListener {
            findViewById<TextView>(R.id.tvData).setText(Date(System.currentTimeMillis()).toString())

            graphQLDemo()
        }
    }

    fun graphQLDemo() {
        val serverUrl = "https://api.spacex.land/graphql/"
        val okHttpClient = OkHttpClient.Builder().build()
        val apolloClient = ApolloClient.builder()
            .serverUrl(serverUrl)
            .okHttpClient(okHttpClient)
            .build()
        var call: ApolloQueryCall<RocketsQuery.Data> = apolloClient!!.query(RocketsQuery())

        call.enqueue(object : ApolloCall.Callback<RocketsQuery.Data>() {
            override fun onResponse(response: Response<RocketsQuery.Data>) {
                runOnUiThread {
                    findViewById<TextView>(R.id.tvData).text = ""
                    response.data?.rockets()?.forEach {
                        findViewById<TextView>(R.id.tvData).append(
                            "${it.id()}, ${ it.name() },\n${ it.description()}\n\n"
                        )
                    }
                }
            }

            override fun onFailure(e: ApolloException) {
                e.printStackTrace()
            }
        })
    }




}