package com.example.mypass.sharedViewModel

import com.example.mypass.R

data class site_icon(
    var name: String,
    var icon: Int
)

fun iconList(): List<site_icon> {
    val icons = mutableListOf<site_icon>()

    // Add icons to the list
    icons.add(site_icon("facebook", R.drawable.facebook))
//    icons.add(site_icon("instagram", R.drawable.instagram))
    icons.add(site_icon("google", R.drawable.google))
    icons.add(site_icon("twitter", R.drawable.twitter_x))
    icons.add(site_icon("samsung", R.drawable.samsung))
    icons.add(site_icon("mega", R.drawable.mega))
    icons.add(site_icon("microsoft", R.drawable.microsoft))
    icons.add(site_icon("paypal", R.drawable.paypal))
    icons.add(site_icon("snap chat", R.drawable.snapchat))
    icons.add(site_icon("games", R.drawable.games))
    icons.add(site_icon("discord", R.drawable.discord))

    return icons
}