package com.example.sunopodcast.repository

import com.example.sunopodcast.dataclasestorecievefromapi.loginclass
import com.example.sunopodcast.dataclasestorecievefromapi.registeruser
import com.example.sunopodcast.dataclasestorecievefromapi.replyfromserver
import com.example.sunopodcast.dataclasestorecievefromapi.response
import retrofit2.Response

interface Repository {
    suspend fun donetworkcall(): response?

    suspend fun registertheuser(registeruser: registeruser):Response<replyfromserver>

    suspend fun loginuser(loginclass: loginclass):Response<replyfromserver>

    suspend fun authenticate(token:String): Response<replyfromserver>
}