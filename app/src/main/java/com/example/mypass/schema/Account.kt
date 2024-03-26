package com.example.mypass.schema

data class Account(
    var site: String,
    val user: List<User>
) {
    data class User(
        val userName:String,
        val email: String,
        val pass: String,
//        val tag: String
    )
}