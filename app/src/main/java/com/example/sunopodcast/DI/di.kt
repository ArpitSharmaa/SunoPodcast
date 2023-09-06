package com.example.sunopodcast.DI

import android.app.Application
import android.content.Context
import com.example.sunopodcast.Apicalls.ApiServices
import com.example.sunopodcast.repository.Repository
import com.example.sunopodcast.repository.Repositoryimp

import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object dimodule {
    @Provides
    @Singleton
    fun providecontext():Context{
        return Application().applicationContext
    }
    @Provides
    @Singleton
    fun provideapi(): ApiServices {
        val base_url ="http://192.168.1.15:8080"
        val gson = GsonBuilder()
            .setLenient()
            .create()
        return Retrofit.Builder().baseUrl(base_url).addConverterFactory(
            GsonConverterFactory.create(gson))
            .build().create(ApiServices::class.java)
    }
    @Provides
    @Singleton
    fun provideRepository(apiServices: ApiServices): Repository {
        return Repositoryimp(apiServices)
    }


//    @Provides
//    @Singleton
//    fun provideMyViewModel(): MyViewModel {
//        return
//    }

}
