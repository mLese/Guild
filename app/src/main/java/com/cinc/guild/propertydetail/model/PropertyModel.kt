package com.cinc.guild.propertydetail

import com.cinc.guild.PropertyDetail
import com.cinc.guild.propertydetail.model.api.API
import com.cinc.guild.propertydetail.model.api.Property
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class PropertyModel : PropertyDetail.PropertyModel {

    private val baseUrl = "https://www.searchstarlingcityareahomes.com"
    private val api: API
    private var property = Property()

    init {
        api = createAPIService(baseUrl)
    }

    // Fetch Property From Network
    override fun fetchProperty(pdid: String,
                               propertyCallback: (property: Property) -> Unit,
                               errorCallback: (errorMessage: String) -> Unit) {
        val call = api.getPropertyDetails(pdid)
        call.enqueue(object : Callback<Property> {
            override fun onResponse(call: Call<Property>, response: Response<Property>) {
                if (response.isSuccessful) {
                    val propertyResponse = response.body() as Property
                    property = propertyResponse
                    propertyCallback(propertyResponse)
                } else {
                    errorCallback("Received Null Property")
                }
            }

            override fun onFailure(call: Call<Property>, t: Throwable) {
                // Handle Error From Network
                errorCallback("Error Fetchin Property: " + t.localizedMessage)
            }
        })
    }

    // Toggle Like Status of Current Property
    override fun propertyLikeToggle(): Boolean {
        property.reaction = if (property.reaction == 0) 1 else 0
        return property.reaction != 0
    }

    // Setup API Service
    private fun createAPIService(baseUrl: String): API {
        val client = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .connectTimeout(30, TimeUnit.SECONDS)
                .build()

        val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .baseUrl(baseUrl)
                .client(client)
                .build()

        return retrofit.create(API::class.java)
    }
}