package com.example.sunopodcast.dataclasestorecievefromapi

data class registeruser(

    val email :String,
    val password :String,
    val fullname : String
)

data class loginclass(
    val email: String,
    val password: String
)