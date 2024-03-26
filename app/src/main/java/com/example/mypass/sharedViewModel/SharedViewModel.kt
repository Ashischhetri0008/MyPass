package com.example.mypass.sharedViewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val _navigationArguments = mutableStateOf<String>("")
    val navigationArguments: State<String> = _navigationArguments
    private val _path = mutableStateOf<String>("")
    val path: State<String> = _path

    fun updateNavigationArguments(newArgs: String) {
        _navigationArguments.value = newArgs
    }
    fun updatePath(newArgs: String) {
        _path.value = newArgs
    }
}