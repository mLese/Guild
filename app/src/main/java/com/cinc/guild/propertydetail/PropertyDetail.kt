package com.cinc.guild.propertydetail

import com.cinc.guild.propertydetail.model.PropertyDetailViewModel

interface PropertyDetail {
    interface View {
        fun displayProperty(property: PropertyDetailViewModel)
        fun setLiked(liked: Boolean)
        fun displayError(errorMessage: String)
    }

    interface Presenter {
        fun fetchProperty(pdid: String)
        fun propertyLikeToggleClicked()
    }

    interface PropertyModel {
        fun fetchProperty(pdid: String,
                          propertyCallback: (property: PropertyDetailViewModel) -> Unit,
                          errorCallback: (errorMessage: String) -> Unit)
        fun propertyLikeToggle(): Boolean
    }
}