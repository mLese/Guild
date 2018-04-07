package com.cinc.guild.propertydetail

import com.cinc.guild.propertydetail.model.api.Property

interface PropertyDetail {
    interface View {
        fun displayProperty(property: Property)
        fun setLiked(liked: Boolean)
        fun displayError(errorMessage: String)
    }

    interface Presenter {
        fun fetchProperty(pdid: String)
        fun propertyLikeToggleClicked()
    }

    interface PropertyModel {
        fun fetchProperty(pdid: String,
                          propertyCallback: (property: Property) -> Unit,
                          errorCallback: (errorMessage: String) -> Unit)
        fun propertyLikeToggle(): Boolean
    }
}