package com.yusufaydin.mobilliumcase.service

import com.yusufaydin.mobilliumcase.model.DoctorModel
import retrofit2.http.GET

interface DoctorAPI {
    @GET("doctors.json")
    suspend fun getDoctorList(): DoctorModel
}