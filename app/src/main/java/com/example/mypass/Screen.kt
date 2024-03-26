package com.example.mypass

sealed class Screen(val route: String)  {
    object Home: Screen(route = "home")
    object Detail: Screen(route = "detail/{itemId}")
    object AddData: Screen(route ="addData/{jsonString}")
}