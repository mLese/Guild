package com.cinc.guild

import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class PropertyDetailActivity : AppCompatActivity() {

    // Screen Configuration
    val pdid = "FMLSRESALL590241548396306"
    val baseUrl = "https://www.searchstarlingcityareahomes.com"
    val loggingTag = "Property Detail"

    // API Service
    lateinit var api: API

    // Property
    lateinit var property: Property
    var favorite = false

    // Views
    lateinit var toolbar: Toolbar
    lateinit var fab: FloatingActionButton
    lateinit var price: TextView
    lateinit var beds: TextView
    lateinit var baths: TextView
    lateinit var sqft: TextView
    lateinit var description: TextView
    lateinit var propertyImage: ImageView
    lateinit var collapsingToolbarLayout: CollapsingToolbarLayout

    // Activity Creation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupView()
        setSupportActionBar(toolbar)
        setupFAB(fab)

        api = createAPIService()
        fetchAndDisplayProperty(api)
    }

    // Setup View
    private fun setupView() {
        setContentView(R.layout.activity_property_detail)
        toolbar = findViewById(R.id.toolbar)
        fab = findViewById(R.id.fab)
        price = findViewById(R.id.price)
        beds = findViewById(R.id.beds)
        baths = findViewById(R.id.baths)
        sqft = findViewById(R.id.sqft)
        description = findViewById(R.id.description)
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar_layout)
        propertyImage = findViewById(R.id.property_image)
    }

    // Setup FAB
    private fun setupFAB(fab: FloatingActionButton) {
        fab.setOnClickListener({ view ->
            run {
                favorite = !favorite
                fab.setImageResource(if (favorite) R.drawable.ic_favorite_white_24dp else R.drawable.ic_favorite_border_white_24dp)
                Snackbar.make(view, "Property " + if (favorite) "Liked" else "Unliked", Snackbar.LENGTH_SHORT)
                        .setAction("Undo", null).show()
            }
        })
    }

    // Setup API Service
    private fun createAPIService(): API {
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

    // Fetch and Display Property
    private fun fetchAndDisplayProperty(api: API) {
        val call = api.getPropertyDetails(pdid)
        call.enqueue(object : Callback<Property> {
            override fun onResponse(call: Call<Property>, response: Response<Property>) {
                if (response.isSuccessful) {
                    property = response.body() as Property

                    setToolbarTitle(property)
                    setBannerImage(property)
                    displaySummary(property)
                    displayDescription(property)
                } else {
                    Log.e(loggingTag, "Received Null Property")
                }
            }

            override fun onFailure(call: Call<Property>, t: Throwable) {
                // Handle Error From Network
                showError("Error Fetchin Property")
                Log.e(loggingTag, "Network Error: " + t.localizedMessage)
            }
        })
    }

    // Set Toolbar Title
    private fun setToolbarTitle(property: Property) {
        collapsingToolbarLayout.isTitleEnabled = false
        toolbar.title = "${property.streetAddress} - ${property.county}"
    }

    // Set Banner Image
    private fun setBannerImage(property: Property) {
        Glide.with(this).load("http://" + property.media[0].mediaUrl).into(propertyImage)
    }

    // Display Property Summary
    private fun displaySummary(property: Property) {
        price.text = property.priceFriendly
        beds.text = "${property.beds} Beds"
        baths.text = "${property.baths} Baths"
        sqft.text = "${property.sqFtFriendly} Sqft"
    }

    // Display Property Description
    private fun displayDescription(property: Property) {
        description.text = property.remarks

    }

    // Show Error Message
    private fun showError(errorMessage: String) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
    }

}
