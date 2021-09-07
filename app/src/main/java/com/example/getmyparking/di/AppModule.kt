package com.example.getmyparking.di

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.getmyparking.MyApplication
import com.example.getmyparking.network.API.BASE_URL
import com.example.getmyparking.network.ParkingApi
import com.example.getmyparking.network.WebServiceProvider
import com.example.getmyparking.repository.HMACSHA256Generator
import com.example.getmyparking.repository.HMACSHA256GeneratorImpl
import com.example.getmyparking.utils.EnumConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideWebServiceProvider(
        @ApplicationContext app: Context
    ) = WebServiceProvider(app)


    @Singleton
    @Provides
    fun provideParkingApi(): ParkingApi =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(EnumConverterFactory())
            .baseUrl(BASE_URL)
            .build()
            .create(ParkingApi::class.java)


    @RequiresApi(Build.VERSION_CODES.N)
    @Provides
    @Singleton
    fun provideHMACSHA256Generator():HMACSHA256Generator =
        HMACSHA256GeneratorImpl()
}