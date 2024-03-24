package com.example.mypass.models

import android.os.Parcelable

data class Accounts(
    val id: Int,
    val site: String,
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