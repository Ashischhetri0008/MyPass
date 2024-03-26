package com.example.mypass


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.util.Log
import android.widget.Toast
import androidx.core.net.toUri
import com.example.mypass.schema.Account
import com.google.gson.Gson
import java.io.File

const val PICK_JSON_FILE = 3
class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController
    private lateinit var jsonDataDir:File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val userList = listOf(
                Account(
                    1,
                    "facebook",
                    listOf(
                        Account.User(1, "ashis","user1@example.com", "password1")
                    )
                ),
                Account(
                    1,
                    "instagram",
                    listOf(
                        Account.User(1, "ashis","user1@example.com", "password1")
                    )
                )
            )

            val downloadDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            if (downloadDir.exists() && downloadDir.isDirectory) {
                jsonDataDir = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    "DataBase"
                )
                if (jsonDataDir.exists() && jsonDataDir.isDirectory) {
                    val fileName = getDataString(this,"jsonFileName").toString()
                    val fileExists = checkIfFileExists(jsonDataDir,fileName ) // check for data.json file
                    if (fileExists) {
                        if(File(jsonDataDir,fileName).canRead()){
                            showToast("read success")
                            navController = rememberNavController()
                            SetupNavGraph(navController = navController)
                        }else{
                            showToast("read unsuccess")
                            openJsonFile(jsonDataDir.toUri(),this)
                        }
                    } else {
                        val file2 = File(jsonDataDir,"data.json" ) // check for data.json file
                        if (file2.exists()) {
                            openJsonFile(jsonDataDir.toUri(),this)
                        }else{
                            createJsonData(directory = jsonDataDir,userList)
                            val file=File(jsonDataDir,getDataString(this,"jsonFileName").toString())
                            if(file.exists() && file.length()>0){
                                navController = rememberNavController()
                                SetupNavGraph(navController = navController)
                            }
                        }
                    }
                }else{
                    val created = jsonDataDir.mkdir()
                    // Folder created successfully
                    if (created) {
                        showToast("Folder 'DataBase' created successfully.")
                        createJsonData(directory = jsonDataDir,userList)
                        val file=File(jsonDataDir,getDataString(this,"jsonFileName").toString())
                        if(file.exists() && file.length()>0){
                            navController = rememberNavController()
                            SetupNavGraph(navController = navController)
                        }
                    } else {
                        // Failed to create folder
                        showToast("Failed to create folder 'DataBase'.")
                    }
                }

            }
        }
    }
    fun openJsonFile(pickerInitialUri: Uri, activity: Activity) {
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
                    // Process the JSON content
                    val gson = Gson()
                    val accountsList= gson.fromJson(jsonContent, Array<Account>::class.java).toList()
                    showToast("dataDir is "+jsonDataDir.toString())
                    createJsonData(directory = jsonDataDir, userList = accountsList)

                    Log.d("MainActivity", "JSON content: $jsonContent")
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

    private fun createJsonData(directory: File, userList:List<Account>){
        val fileName = "data.json"
        val baseFileName = "data"
        var index = 1
        var dataFile: File
        val file = File(directory, fileName)
        if(file.exists()){
            val jsonString = Gson().toJson(userList)
            val data01=File(directory,"data01.json")
            if(data01.exists()){
                do {
                    val fileName2 = "$baseFileName${String.format("%02d", index)}.json"
                    dataFile = File(directory, fileName2)
                    index++
                } while (dataFile.exists())
                try {
                    dataFile.writeText(jsonString)
                    saveDataString(this,"jsonFileName",dataFile.name)

                } catch (e: Exception) {
                    showToast("${e.message}")
                }
            }else{
                dataFile=File(directory,"data01.json")
                showToast("file: "+dataFile.name)
                try {
                    dataFile.writeText(jsonString)
                    saveDataString(this,"jsonFileName",dataFile.name)

                } catch (e: Exception) {
                    showToast("${e.message}")
                }

            }
//             Convert the list of User objects to a JSON string using Gson
        }else{
            try {
                // Convert the list of User objects to a JSON string using Gson
                val jsonString = Gson().toJson(userList)
                // Write the JSON string to a file
                val file = File(directory, fileName)
                try {
                    file.writeText(jsonString)
                    saveDataString(this,"jsonFileName",fileName)
                } catch (e: Exception) {
                    showToast("${e.message}")
                }

                showToast("data.json created")

            } catch (e: Exception) {
                Log.e("MainActivity", "Error creating JSON file: ${e.message}")
            }
        }

    }


    // Show Toast Meassage
    private fun showToast(message: String) {

        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun checkIfFileExists(directory: File, fileName: String): Boolean {
        val file = File(directory, fileName)
        return file.exists()
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


}

