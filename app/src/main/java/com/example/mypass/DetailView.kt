package com.example.mypass

import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mypass.schema.Account
import com.google.gson.Gson
import java.io.File


@Composable
fun DetailsView(site: String?){

    var listAcc:List<Account> = emptyList()
    var listUsers:List<Account.User> = emptyList()
    val context= LocalContext.current
    val downloadDir =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    if (downloadDir.exists() && downloadDir.isDirectory) {
        val jsonDataDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "DataBase"
        )
        if (jsonDataDir.exists() && jsonDataDir.isDirectory){
            val fileName= "data.json"
            val file =  File(jsonDataDir, fileName) // check for data.json file
            if(file.exists()){
//                    textSample(t1 = getDataString(context,"jsonFileName").toString())
                listAcc=readJsonData(directory = jsonDataDir)
                val exaccount=listAcc.find { it.site == site }
                if (exaccount != null) {
                    listUsers=exaccount.user
                }


            }else{
                // create data.json file if not existed
                showToast("No ${getDataString(context,"jsonFileName")}")

            }

        }else{
            showToast("no Dir DataBase")
        }
    } else {
        showToast("Download directory not found")
    }
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(start = 12.dp, top = 12.dp, end = 12.dp, bottom = 0.dp)
    ){

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)){
            items(listUsers) { user ->
                DetailsCard(user.userName, user.email,user.pass)
            }
        }
    }
}

// Read JSON data from the file
@Composable
private fun readJsonData(directory: File): List<Account> {
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
            //            val accountsArray = gson.fromJson(jsonContent, Accounts::class.java)
            val accountsList = gson.fromJson(jsonContent, Array<Account>::class.java).toList()
            return accountsList
        }catch (e:Exception){
            Log.d("MainActivity",e.message.toString())
        }

    } else {
        Log.e("MainActivity", "JSON file does not exist.")
    }
    return emptyList()
}


@Composable
fun DetailsCard(userName:String,email: String,pass: String, modifier: Modifier = Modifier) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 0.dp, vertical = 0.dp),
        shape = RoundedCornerShape(15.dp)
    ){
        Column(modifier = Modifier.padding(10.dp)) {
            Text(modifier = Modifier.padding(5.dp),text = "User : $userName")
            Text(modifier = Modifier.padding(5.dp),text = "Email : $email")
            Text(modifier = Modifier.padding(5.dp),text = "Pass : $pass")
        }
    }
}

// Show Toast Meassage
@Composable
private fun showToast(message: String) {
    val context= LocalContext.current
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}
