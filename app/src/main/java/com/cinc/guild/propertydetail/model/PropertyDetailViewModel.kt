package com.cinc.guild.propertydetail.model

import com.cinc.guild.propertydetail.model.api.Property

class PropertyDetailViewModel(property: Property) {
    val bannerImageUrl = "http://${property.media[0].mediaUrl}"
    val title = "${property.streetAddress} ${property.county}"
    val price = property.priceFriendly
    val beds = property.beds.toString()
    val baths = property.baths.toString()
    val squareFootage = "${property.sqFtFriendly} sqft"
    val description = property.remarks
}