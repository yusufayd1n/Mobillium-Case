package com.yusufaydin.mobilliumcase.model


import com.google.gson.annotations.SerializedName

data class Doctor(
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("image")
    val image: Image,
    @SerializedName("user_status")
    val userStatus: String
)