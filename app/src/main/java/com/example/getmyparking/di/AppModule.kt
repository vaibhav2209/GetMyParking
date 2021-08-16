package com.example.getmyparking.di

import android.content.Context
import com.example.getmyparking.MyApplication
import com.example.getmyparking.network.WebServiceProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideWebServiceProvider(
        @ApplicationContext app: Context
    ) = WebServiceProvider(app)

    

}