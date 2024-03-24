package com.example.mypass

import android.content.Context
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mypass.models.Accounts
import com.google.gson.Gson
import java.io.File

@Composable
fun HomeScreen(navController: NavHostController){
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(start = 12.dp, top = 12.dp, end = 12.dp, bottom = 0.dp),

    ){
        var listAcc:List<Accounts> = emptyList()
        val context= LocalContext.current
        val downloadDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        if (downloadDir.exists() && downloadDir.isDirectory) {
            val jsonDataDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                "DataBase"
            )
            if (jsonDataDir.exists() && jsonDataDir.isDirectory){
                val fileName= "data.json"
                val file =  File(jsonDataDir, fileName) // check for data.json file
                if(file.exists()){
//                    textSample(t1 = getDataString(context,"jsonFileName").toString())
                    listAcc=readJsonData(directory = jsonDataDir)

                }else{
                    // no data.json file
                    showToast("No ${getDataString(context,"jsonFileName")}")
                }

            }else{
                // DataBase does not exist
                showToast("no Dir DataBase")
            }
        } else {
            // Download dir not Found
            showToast("Download directory not found")
        }
        Scaffold(listAcc,navController){}
    }
}


// Read JSON data from the file
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

// Show Toast Meassage
@Composable
private fun showToast(message: String) {
    val context= LocalContext.current
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}

// Function to retrieve a string value from SharedPreferences using a custom key
fun getDataString(context: Context, key: String): String? {
    val sharedPreferences = context.getSharedPreferences("dataFileName", Context.MODE_PRIVATE)
    return sharedPreferences.getString(key, null)
}

@Composable
fun Scaffold(listAcc:List<Accounts>,navController:NavHostController,function: () -> Unit) {

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(route = "addData")
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(listAcc) { item ->
                Account(item.id, item.site, navController)
            }
        }
    }
}

@Composable
fun Account(itemId: Int, site: String,navController: NavHostController){
    Card(modifier = Modifier
        .fillMaxWidth()
        .clickable { navController.navigate(route = "detail/$itemId") }
        .padding(start = 0.dp, top = 0.dp, end = 0.dp, bottom = 0.dp),
        shape = RoundedCornerShape(15.dp)
    ){
        Row(modifier = Modifier.padding(10.dp)
        ) {
            Icon(painter = painterResource(id = R.drawable.outline_account_circle_24),
                contentDescription = stringResource(id = R.string.account_icon))
            Text(modifier = Modifier
                .padding(10.dp)
                .align(Alignment.CenterVertically),
                fontSize = 24.sp,
                text = site)
        }

    }
}


@Composable
@Preview
fun HomeScreenPreview(){
    HomeScreen(navController = rememberNavController())
}