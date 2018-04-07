package com.cinc.guild

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.widget.TextView
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

    lateinit var toolbar: Toolbar
    lateinit var fab: FloatingActionButton
    lateinit var price: TextView
    lateinit var sqft: TextView
    lateinit var description: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_property_detail)
        val activity = this

        toolbar = findViewById(R.id.toolbar)
        fab = findViewById(R.id.fab)
        price = findViewById(R.id.price)
        sqft = findViewById(R.id.sqft)
        description = findViewById(R.id.description)

        setSupportActionBar(toolbar)

        var favorite = false

        fab.setOnClickListener({ view ->
            run {
                favorite = ! favorite
                fab.setImageResource(if (favorite) R.drawable.ic_favorite_white_24dp else R.drawable.ic_favorite_border_white_24dp)
                Snackbar.make(view, "Property " + if (favorite) "Liked" else "Unliked", Snackbar.LENGTH_SHORT)
                        .setAction("Undo", null).show()
            }
        })

        val call = api.getPropertyDetails("FMLSRESALL590241548396306")
        call.enqueue(object: Callback<Property> {
            override fun onResponse(call: Call<Property>, response: Response<Property>) {
                if (response.isSuccessful) {
                    property = response.body() as Property

                    collapsing_toolbar_layout.isTitleEnabled = false
                    toolbar.title = "${property.streetAddress} ${property.county}"

                    Glide.with(activity).load("http://" + property.media[0].mediaUrl).into(property_image)
                    price.text = property.priceFriendly
                    beds.text = "${property.beds} Beds"
                    baths.text = "${property.baths} Baths"
                    sqft.text = "${property.sqFtFriendly} Sqft"
                    description.text = property.remarks

                } else {
                    Log.e("Property Detail", "Received Null Property")
                }
            }

            override fun onFailure(call: Call<Property>, t: Throwable) {
                Log.e("Property Detail", "Network Error: " + t.localizedMessage)
            }
        })
    }

}
