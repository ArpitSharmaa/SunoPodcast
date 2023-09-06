package com.example.sunopodcast.Viewmodel


import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sunopodcast.dataclasestorecievefromapi.loginclass
import com.example.sunopodcast.dataclasestorecievefromapi.registeruser
import com.example.sunopodcast.dataclasestorecievefromapi.replyfromserver
import com.example.sunopodcast.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow

import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(
   private val repository: Repository,
   private val application: Application
): ViewModel(){
    var currentuser = false
    val _podcastlist = MutableSharedFlow<List<String>>()
    val padcastlist = _podcastlist.asSharedFlow()
    val _sharedtoast = MutableSharedFlow<String>()
    val sharedtoast = _sharedtoast.asSharedFlow()
    val handler= CoroutineExceptionHandler { coroutineContext, throwable ->
        viewModelScope.launch {
            _sharedtoast.emit("Failed to Establish Connection")
        }

    }
   fun getallpodcastlist(){

       viewModelScope.launch(Dispatchers.Main + handler) {
           val list = asynccallforlist()
           _podcastlist.emit(list)
       }
   }

    private suspend fun asynccallforlist(): List<String> {
       val z=  viewModelScope.async {
            repository.donetworkcall()
        }
        return z.await()?.key ?: emptyList()
    }

    suspend fun registersentusertorepo(registeruser: registeruser):Response<replyfromserver>?{

        val z = viewModelScope.async (Dispatchers.Main) {
              callregistermethod(registeruser)
//            Toast.makeText(application.applicationContext, "${z?.body()?.message}", Toast.LENGTH_SHORT).show()

        }
        return z.await()
    }

    private suspend fun callregistermethod(registeruser: registeruser): Response<replyfromserver>? {
        val wait=
        viewModelScope.async(Dispatchers.IO) {
            repository.registertheuser(registeruser)
        }

        return wait.await()
    }

    suspend fun logintheuser(loginclass: loginclass): Response<replyfromserver> {
       val z =  viewModelScope.async(Dispatchers.IO) {
            repository.loginuser(loginclass)
        }
        return z.await()
    }

    suspend fun authenticate(token:String): Response<replyfromserver> {
        val retreat = viewModelScope.async {
            repository.authenticate(
                token
            )
        }
        return retreat.await()
    }
}