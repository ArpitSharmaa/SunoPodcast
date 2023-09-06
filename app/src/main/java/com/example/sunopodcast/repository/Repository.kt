package com.example.sunopodcast.repository

import com.example.sunopodcast.Apicalls.ApiServices
import com.example.sunopodcast.dataclasestorecievefromapi.loginclass
import com.example.sunopodcast.dataclasestorecievefromapi.registeruser
import com.example.sunopodcast.dataclasestorecievefromapi.replyfromserver
import com.example.sunopodcast.dataclasestorecievefromapi.response
import com.example.sunopodcast.repository.Repository
import retrofit2.Response

class Repositoryimp(private val apiServices: ApiServices) : Repository {

        override suspend fun donetworkcall(): response? {

               return apiServices.getallpodcastlist().body()
        }

        override suspend fun registertheuser(registeruser : registeruser): Response<replyfromserver> {

              return apiServices.registeruser(registeruser)
        }

    override suspend fun loginuser(loginclass: loginclass): Response<replyfromserver> {

            return apiServices.loginuser(loginclass)
    }

    override suspend fun authenticate(token: String): Response<replyfromserver> {

            return apiServices.authenticate(token)
    }
}