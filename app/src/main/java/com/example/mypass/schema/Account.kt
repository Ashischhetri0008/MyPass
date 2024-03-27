package com.example.mypass.schema

data class Account(
    var site: String,
    val users: List<User>
) {
    data class User(
        val webSite:String,
        val userName:String,
        val email: String,
        val password: String,
//        val tag: String
    )
}