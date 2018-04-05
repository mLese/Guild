package com.cinc.guild

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.bumptech.glide.Glide
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_property_detail.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class PropertyDetailActivity : AppCompatActivity() {

    val loggingTag = "PropertyDetail"

    val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(30, TimeUnit.SECONDS)
            .build()

    val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .baseUrl("https://www.searchstarlingcityareahomes.com")
            .client(client)
            .build()

    val api = retrofit.create(API::class.java)

    lateinit var property: Property

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_property_detail)

        val activity = this

        val call = api.getPropertyDetails("FMLSRESALL590241548396306")
        call.enqueue(object: Callback<Property> {
            override fun onResponse(call: Call<Property>, response: Response<Property>) {
                if (response.isSuccessful) {
                    property = response.body() as Property
                    Log.v(loggingTag, property.toString())

                    Glide.with(activity).load("http://" + property.media[0].mediaUrl).into(property_image)

                } else {
                    Log.e(loggingTag, "Received Null Property")
                }
            }

            override fun onFailure(call: Call<Property>, t: Throwable) {
                Log.e(loggingTag, "Network Error: " + t.localizedMessage)
            }
        })
    }

}
