package com.yusufaydin.mobilliumcase.model


import com.google.gson.annotations.SerializedName

data class DoctorModel(
    @SerializedName("doctors")
    val doctors: List<Doctor>
)