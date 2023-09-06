package com.example.sunopodcast.Apicalls


import com.example.sunopodcast.dataclasestorecievefromapi.loginclass
import com.example.sunopodcast.dataclasestorecievefromapi.registeruser
import com.example.sunopodcast.dataclasestorecievefromapi.replyfromserver
import com.example.sunopodcast.dataclasestorecievefromapi.response
import retrofit2.Response
import retrofit2.http.Body

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiServices {

    @GET("/podcasts")
    suspend fun getallpodcastlist():Response<response>

    @POST("register")
    suspend fun registeruser(@Body registeruser: registeruser):Response<replyfromserver>

    @POST("/login")
    suspend fun loginuser(@Body loginclass: loginclass):Response<replyfromserver>

    @GET("authenticate")
    suspend fun authenticate(@Header("Authorization") token:String) : Response<replyfromserver>

    @GET("/secret")
    suspend fun getemail (@Header("Authorization") token:String):Response<replyfromserver>

}