package com.example.mypass.schema

data class Account(
    var head: String,
    var tags: List<String>,
    val users: List<User>

) {
    data class User(
        val userName:String,
        val webSite:String,
        val email: String,
        val password: String,
    )
}