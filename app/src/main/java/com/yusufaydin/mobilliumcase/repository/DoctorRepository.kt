package com.yusufaydin.mobilliumcase.repository

import com.yusufaydin.mobilliumcase.model.DoctorModel
import com.yusufaydin.mobilliumcase.service.DoctorAPI
import com.yusufaydin.mobilliumcase.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import java.lang.Exception
import javax.inject.Inject

@ActivityScoped
class DoctorRepository @Inject constructor(
    private val api: DoctorAPI
) {
    suspend fun getDoctorList(): Resource<DoctorModel> {
        val response = try {
            api.getDoctorList()
        } catch (e: Exception) {
            return Resource.Error("Error.")
        }
        return Resource.Success(response)
    }


}