package com.cinc.guild.propertydetail.presenter

import com.cinc.guild.propertydetail.PropertyDetail
import com.cinc.guild.propertydetail.PropertyDetailModel

class PropertyDetailPresenter(val view: PropertyDetail.View): PropertyDetail.Presenter {

    val propertyModel = PropertyDetailModel()

    // Fetch Property From Model
    override fun fetchProperty(pdid: String) {
        propertyModel.fetchProperty(pdid, view::displayProperty, view::displayError)
        //propertyModel.fetchProperty(pdid, { property -> view.displayProperty(property) }, { errorMessage -> view.displayError(errorMessage) })
    }

    // Toggle Property Reaction Status
    override fun propertyLikeToggleClicked() {
        val liked = propertyModel.propertyLikeToggle()
        view.setLiked(liked)
    }
}