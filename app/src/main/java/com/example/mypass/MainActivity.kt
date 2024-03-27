package com.example.mypass


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mypass.InputsUI.getDataFileName
import com.example.mypass.InputsUI.setDataFileName
import com.example.mypass.schema.Account
import com.example.mypass.sharedViewModel.SharedViewModel
import com.google.gson.Gson
import java.io.File

const val PICK_JSON_FILE = 3
var pick_file_name=""
class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController
    private lateinit var jsonDataDir:File
    private lateinit var viewModel: SharedViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            viewModel= viewModel()
            val downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            if (downloadDir.exists() && downloadDir.isDirectory) {
                jsonDataDir = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    "DataBase"
                )
                if (jsonDataDir.exists() && jsonDataDir.isDirectory) {

                    navController = rememberNavController()

                    val filename = getDataFileName(this, "jsonFileName").toString()
                    val file=File(jsonDataDir,filename)
                    if(file.exists()){
                        if(file.canWrite()&&file.canRead()) {
                            val defaultName="data.json"
                            val file2=File(jsonDataDir,defaultName)
                            if(file2.exists()){
                                showToast("Data: ${filename}")
                                SetupNavGraph(navController = navController,viewModel)
                            }else {
                                val jsonString=file.readText()
                                val listAcc=Gson().fromJson(jsonString, Array<Account>::class.java).toList()
                                createJsonData(jsonDataDir,listAcc)
                                SetupNavGraph(navController = navController,viewModel)
                            }

                        }else {
                            PickAndCreate(navController)
                        }

                    }else{
                        PickAndCreate(navController)
                    }
                }else{
                    val created = jsonDataDir.mkdir()
                    // Folder created successfully
                    if (created) {
                        createJsonData(jsonDataDir, emptyList())
                    } else {
                        // Failed to create folder
                        showToast("Failed to create folder 'DataBase'.")
                    }
                }
            }
        }
    }

    @Composable
    fun PickAndCreate(navController: NavHostController){
        var clicked by remember { mutableStateOf(false) }
        var pick by remember { mutableStateOf(false) }
        Column (modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center // Center vertically

        ){

            Row(modifier = Modifier
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                // Button to submit the form
                Button(
                    onClick = {
                        pick=true
                        clicked=true
                    }) {
                    Text(text = "Pick A File")
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(modifier = Modifier
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                // Button to submit the form
                Button(
                    onClick = {
                        pick=false
                        clicked=true
                    }) {
                    Text(text = "Create New")
                }
            }

        }
        if(clicked){
            if(pick){
                openJsonFile(jsonDataDir.toUri(),this)
                showToast("Data: ${getDataFileName(this,"jsonFileName").toString()}")
                SetupNavGraph(navController = navController,viewModel)

            }else{
                createJsonData(jsonDataDir, emptyList())
                showToast("Data: ${getDataFileName(this,"jsonFileName").toString()}")
                SetupNavGraph(navController = navController,viewModel)
            }
        }
    }


    //----------------------------------------- File Picker Fucntion ---------------------------
    fun openJsonFile(pickerInitialUri: Uri, activity: MainActivity) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/json" // Specify the MIME type for JSON files

            // Optionally, specify a URI for the file that should appear in the
            // system file picker when it loads.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri)
            }
        }

        activity.startActivityForResult(intent, PICK_JSON_FILE)
    }

    // Handle the result in onActivityResult method
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_JSON_FILE && resultCode == Activity.RESULT_OK && data != null) {
            val uri = data.data
            if (uri != null) {
                // Read the selected JSON file
                val jsonContent = readFileFromUri(uri)

                if (jsonContent != null) {

                    try{
                        val gson = Gson()
                        val accountsList = gson.fromJson(jsonContent, Array<Account>::class.java).toList()
                        createJsonData(directory = jsonDataDir, userList = accountsList)
                        showToast("Read Success")
                        Log.e("MainActivity", "Conversion to JSON Object Success.")
                    }catch (e:Exception){
                        Log.e("MainActivity", "Conversion to JSON Object Failed.")
                    }
                } else {
                    // Failed to read JSON file
                    Log.e("MainActivity", "Failed to read JSON file")
                    // You can show an error message to the user
                }
            } else {
                // URI is null
                Log.e("MainActivity", "URI is null")
                // Handle null URI
            }
        }
    }

    // ----------------------------------------- Call Function ------------------------------------
    fun readFileFromUri(uri: Uri): String? {
        val contentResolver = applicationContext.contentResolver
        try {
            contentResolver.openInputStream(uri)?.use { inputStream ->
                // Read the data from the input stream
                return inputStream.bufferedReader().use { it.readText() }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
    private fun createJsonData(directory: File, userList:List<Account>) {
        val jsonString = Gson().toJson(userList)
        viewModel.updateNavigationArguments(jsonString)
        val fileName = "data.json"
        val baseFileName = "data"
        var index = 1
        var dataFile: File
        val file = File(directory, fileName)
        if (file.exists()) {
            val data01 = File(directory, "data01.json")
            if (data01.exists()) {
                do {
                    val fileName2 = "$baseFileName${String.format("%02d", index)}.json"
                    dataFile = File(directory, fileName2)
                    index++
                } while (dataFile.exists())
                try {
                    dataFile.writeText(jsonString)
                    setDataFileName(this, "jsonFileName", dataFile.name)
                    showToast("Set: " + dataFile.name)
                } catch (e: Exception) {
                    showToast("${e.message}")
                }
            } else {
                dataFile = File(directory, "data01.json")
                showToast("Set: " + dataFile.name)
                try {
                    dataFile.writeText(jsonString)
                    setDataFileName(this, "jsonFileName", dataFile.name)

                } catch (e: Exception) {
                    showToast("${e.message}")
                }

            }
        } else {
            try {
                file.writeText(jsonString)
                setDataFileName(this, "jsonFileName", fileName)
                showToast("Set: ${file.name}")
            } catch (e: Exception) {
                showToast("${e.message}")
            }

        }
    }


    // Show Toast Meassage
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

}

