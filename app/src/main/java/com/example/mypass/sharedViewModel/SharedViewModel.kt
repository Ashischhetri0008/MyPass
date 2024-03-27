package com.example.mypass.sharedViewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val _navigationArguments = mutableStateOf<String>("")
    val navigationArguments: State<String> = _navigationArguments

    fun updateNavigationArguments(newArgs: String) {
        _navigationArguments.value = newArgs
    }

}