package com.yusufaydin.mobilliumcase.di

import com.yusufaydin.mobilliumcase.repository.DoctorRepository
import com.yusufaydin.mobilliumcase.service.DoctorAPI
import com.yusufaydin.mobilliumcase.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideDoctorRepository(
        api: DoctorAPI
    ) = DoctorRepository(api)

    @Singleton
    @Provides
    fun provideDoctorApi(): DoctorAPI {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(DoctorAPI::class.java)
    }
}