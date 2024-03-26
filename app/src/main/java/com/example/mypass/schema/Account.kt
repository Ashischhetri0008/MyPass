package com.example.mypass.schema

data class Account(
    val id: Int,
    var site: String,
    val user: List<User>
) {
    data class User(
        val id: Int,
        val userName:String,
        val email: String,
        val pass: String,
//        val tag: String
    )
}