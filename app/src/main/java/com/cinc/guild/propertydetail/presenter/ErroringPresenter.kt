package com.cinc.guild.propertydetail.presenter

import com.cinc.guild.propertydetail.PropertyDetail

class ErroringPresenter(val view: PropertyDetail.View): PropertyDetail.Presenter {
    override fun fetchProperty(pdid: String) {
        view.displayError("Error Fetchin Property")
    }

    override fun propertyLikeToggleClicked() {
        view.setLiked(true)
    }
}