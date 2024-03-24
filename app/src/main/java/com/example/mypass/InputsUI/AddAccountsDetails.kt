package com.example.mypass.InputsUI

import android.content.Context
import android.os.Environment
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.mypass.getDataString
import com.example.mypass.models.Accounts
import com.google.gson.Gson
import java.io.File


@Composable
fun AddAccountsDetails(){
    val context= LocalContext.current
    var listAcc:List<Accounts> = emptyList()
    val downloadDir =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    if (downloadDir.exists() && downloadDir.isDirectory) {
        val jsonDataDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "DataBase"
        )
        if (jsonDataDir.exists() && jsonDataDir.isDirectory){
            val fileName=getDataString(context,"jsonFileName").toString()
            val file = File(jsonDataDir, fileName)
            if (file.exists()) {
                listAcc=readJsonData(jsonDataDir)
                textFiled(listAcc = listAcc)
            }else{
                textFiled(listAcc = listAcc)
            }

        }
    }

}

@Composable
private fun readJsonData(directory: File): List<Accounts> {
    val context= LocalContext.current
    val fileName = getDataString(context,"jsonFileName").toString()
    val file = File(directory, fileName)
    if (file.exists()) {
        try {
            // Read JSON content from the file
            val jsonContent = file.readText()
            // Initialize Gson instance
            val gson = Gson()
            // Deserialize JSON to array of Accounts objects
            val accountsList = gson.fromJson(jsonContent, Array<Accounts>::class.java).toList()
            return accountsList
        }catch (e:Exception){
            Log.d("MainActivity",e.message.toString())
        }

    } else {
        Log.e("MainActivity", "JSON file does not exist.")
    }
    return emptyList()
}
fun getDataString(context: Context, key: String): String? {
    val sharedPreferences = context.getSharedPreferences("dataFileName", Context.MODE_PRIVATE)
    return sharedPreferences.getString(key, null)
}

@Composable
fun textFiled(listAcc:List<Accounts>){
    var acc:Accounts
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(start = 12.dp, top = 12.dp, end = 12.dp, bottom = 0.dp)
    ){
        Column {
            var site by remember { mutableStateOf("") }
            OutlinedTextField(
                value = site,
                onValueChange = { site = it },
                label = { Text("Site") }
            )
            var userName by remember { mutableStateOf("") }
            OutlinedTextField(
                value = userName,
                onValueChange = { userName = it },
                label = { Text("User Name") }
            )
            var email by remember { mutableStateOf("") }
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") }
            )
            var pass by remember { mutableStateOf("") }
            OutlinedTextField(
                value = pass,
                onValueChange = { pass = it },
                label = { Text("Password") }
            )
        }

    }


}


@Composable
fun text(t:String){
    Text(text = t)
}

