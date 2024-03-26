package com.example.mypass.InputsUI

import android.content.Context
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.mypass.getDataString
import com.example.mypass.schema.Account
import com.example.mypass.sharedViewModel.SharedViewModel
import com.google.gson.Gson
import java.io.File

@Composable
fun AddAccountsDetails(jsonData: String,viewModel: SharedViewModel){
    val context= LocalContext.current
    var listAcc:List<Account> = emptyList()
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
                listAcc= parsString(jsonData)
                Column {
                    val temp= add_data(listAcc)
                    if(listAcc!=temp){
                        listAcc=temp
                    }
                    SampleText(t = listAcc.toString())
                }

            }else{
            }
        }
    }
}

@Composable
fun add_data(listAcc: List<Account>):List<Account>{
    var updatedList by remember { mutableStateOf(listAcc) }
    Box(modifier = Modifier
        .fillMaxWidth()
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
                label = { Text("Email") },
                leadingIcon = {
                    Icon(Icons.Outlined.AccountCircle, contentDescription = "Account")
                }
            )
            var pass by remember { mutableStateOf("") }
            OutlinedTextField(
                value = pass,
                onValueChange = { pass = it },
                label = { Text("Password") }
            )

            Button(onClick = {
                val newAccount = Account(
                    4,
                    site,
                    listOf(
                        Account.User(1, userName, email, pass)
                    )
                )

                updatedList= add_to_list(updatedList,newAccount)
                site=""
            }) {
                Text(text = "Submit")
            }
        }
    }
    return updatedList
}


private fun add_to_list(listAcc: List<Account>, account: Account): List<Account>{
    if(account.site!="" && account.user[0].userName!="" && account.user[0].email!="" && account.user[0].pass!=""){
        if(listAcc.any(){it.site==account.site}){
            val index=listAcc.indexOf(account)
//            listAcc[index].user.(account.user[0])
            return listAcc
        }else {
            return listAcc.toMutableList().apply { add(account) }
        }
    }
    return listAcc
}

private fun parsString(jsonSting: String): List<Account> {
        try {
            // Initialize Gson instance
            val gson = Gson()
            // Deserialize JSON to array of Accounts objects
            //        sampleText("gson convert: "+accountsList.toString())
            return gson.fromJson(jsonSting, Array<Account>::class.java).toList()
        }catch (e:Exception){
            Log.d("MainActivity",e.message.toString())
        }
    return emptyList()
}


private fun update_data_to_file( userList:List<Account>){
    val fileName = "data.json"
    val file = File(fileName)
    if(file.exists()){
        val jsonString = Gson().toJson(userList)
        val data01=File("data01.json")
        if(data01.exists()){

        }else{


        }
//             Convert the list of User objects to a JSON string using Gson
    }else{

    }

}
@Composable
private fun ShowToast(message: String) {
    val context= LocalContext.current
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}

fun saveDataString(context: Context, newKey: String, value: String) {
    val sharedPreferences = context.getSharedPreferences("dataFileName", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString(newKey, value) // Use the new key to save the value
    editor.apply()
}

// Function to retrieve a string value from SharedPreferences using a custom key
fun getDataString(context: Context, key: String): String? {
    val sharedPreferences = context.getSharedPreferences("dataFileName", Context.MODE_PRIVATE)
    return sharedPreferences.getString(key, null)
}


@Composable
fun SampleText(t:String){
    Text(text = t)
}

