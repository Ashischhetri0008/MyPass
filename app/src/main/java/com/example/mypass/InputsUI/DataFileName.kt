package com.example.mypass.InputsUI

import android.content.Context

// Function to Set a databaseFileName value from SharedPreferences using a custom key
fun setDataFileName(context: Context, newKey: String, value: String) {
    val sharedPreferences = context.getSharedPreferences("dataFileName", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString(newKey, value) // Use the new key to save the value
    editor.apply()
}

// Function to retrieve a databaseFileName value from SharedPreferences using a custom key
fun getDataFileName(context: Context, key: String): String? {
    val sharedPreferences = context.getSharedPreferences("dataFileName", Context.MODE_PRIVATE)
    return sharedPreferences.getString(key, null)
}