package com.cinc.guild.propertydetail.view

import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.cinc.guild.R
import com.cinc.guild.propertydetail.PropertyDetail
import com.cinc.guild.propertydetail.model.api.Property
import com.cinc.guild.propertydetail.presenter.PropertyDetailPresenter

class PropertyDetailActivity : AppCompatActivity(), PropertyDetail.View {

    // Screen Configuration
    val pdid = "FMLSRESALL590241548396306"
    val loggingTag = "Property Detail"

    // Presenter
    val presenter = PropertyDetailPresenter(this)

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
    lateinit var mainLayout: CoordinatorLayout

    // Activity Creation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupView()
        setSupportActionBar(toolbar)
        setupFAB(fab)

        presenter.fetchProperty(pdid)
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
        mainLayout = findViewById(R.id.property_detail_layout)
    }

    // Setup FAB
    private fun setupFAB(fab: FloatingActionButton) {
        fab.setOnClickListener({ _ ->
            run {
                presenter.propertyLikeToggleClicked()
            }
        })
    }

    // Display Property
    override fun displayProperty(property: Property) {
        setToolbarTitle(property)
        setBannerImage(property)
        displaySummary(property)
        displayDescription(property)
    }

    // Set Liked Status on FAB
    override fun setLiked(liked: Boolean) {
        fab.setImageResource(if (liked) R.drawable.ic_favorite_white_24dp else R.drawable.ic_favorite_border_white_24dp)
        Snackbar.make(mainLayout, "Property " + if (liked) "Liked" else "Unliked", Snackbar.LENGTH_SHORT).show()
    }

    // Display Error Message
    override fun displayError(errorMessage: String) {
        Log.e(loggingTag, errorMessage)
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
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

}
