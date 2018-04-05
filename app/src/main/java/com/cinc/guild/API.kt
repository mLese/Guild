package com.cinc.guild

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface API {
    @GET("/api/details/{pdid}")
    fun getPropertyDetails(@Path("pdid") pdid: String) : Call<Property>
}