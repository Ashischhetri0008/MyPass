package com.example.mypass.InputsUI

import android.content.Context
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.ArrowDownward
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.example.mypass.getDataString
import com.example.mypass.schema.Account
import com.example.mypass.sharedViewModel.SharedViewModel
import com.google.gson.Gson
import java.io.File


@Composable
fun AddAccountsDetails(viewModel: SharedViewModel,navController: NavHostController){
    val context= LocalContext.current
    var listAcc by remember {
        mutableStateOf(emptyList<Account>())
    }
    listAcc= parsString(viewModel.navigationArguments.value)
    Column {
        listAcc= add_data(listAcc,navController)
        try{
            viewModel.updateNavigationArguments(Gson().toJson(listAcc))
            update_data_to_file(listAcc,context)
        }catch (e:Exception){
            Log.e("MainActivity","file not updated: ${e.message}")
        }
    }
}

@Composable
fun add_data(listAcc2: List<Account>, navController: NavHostController): List<Account> {
    var updatedList by remember { mutableStateOf(listAcc2) }

    // State variables for managing input values
    var site by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }

    var textFieldSize by remember {
        mutableStateOf(Size.Zero)
    }
    var expanded by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, top = 12.dp, end = 12.dp, bottom = 0.dp)
            .clickable(
                onClick = {
//                    expanded = false
                }
            )
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .onGloballyPositioned { coordinates ->
                        textFieldSize = coordinates.size.toSize()
                    },
                value = site,
                onValueChange = {
                    site = it
                    expanded = true
                },

                singleLine = true,
                trailingIcon = {
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(Icons.Outlined.ArrowDropDown,"Down arrow")
                    }
                },
                label = { Text("Site") }
            )

        }
        Box(
        ){
            Popup() {
                androidx.compose.animation.AnimatedVisibility(visible = expanded) {
                    Card(
                        modifier = Modifier
                            .padding(horizontal = 30.dp)
                            .width(textFieldSize.width.dp),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 6.dp
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {

                        LazyColumn(
                            modifier = Modifier.heightIn(max = 300.dp),
                        ) {

                            if (site.isNotEmpty()) {
                                items(
                                    updatedList.map { it.site }.filter {
                                        it.lowercase()
                                            .contains(site.lowercase())
                                    }
                                ) {
                                    CategoryItems(title = it) { title ->
                                        site = title
                                        expanded = false
                                    }
                                }
                            } else {
                                items(
                                    updatedList.map { it.site }.sorted()
                                ) {
                                    CategoryItems(title = it) { title ->
                                        site = title
                                        expanded = false
                                    }
                                }
                            }

                        }

                    }
                }

            }
        }

        // Text field for user name
        OutlinedTextField(
            value = userName,
            onValueChange = { userName = it },
            label = { Text("User Name") }
        )

        // Text field for email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            leadingIcon = {
                Icon(Icons.Outlined.AccountCircle, contentDescription = "Account")
            }
        )

        // Text field for password
        OutlinedTextField(
            value = pass,
            onValueChange = { pass = it },
            label = { Text("Password") }
        )

        // Button to submit the form
        Button(onClick = {
            val newAccount = Account(
                site.trim(),
                listOf(
                    Account.User(userName.trim(), email.trim(), pass.trim())
                )
            )
            updatedList = add_to_list(updatedList, newAccount)
            navController.navigate("home")
        }) {
            Text(text = "Submit")
        }
    }

    return updatedList
}


private fun add_to_list(listAcc: List<Account>, account: Account): List<Account>{
    if (account.site.isNotBlank() &&
        account.user.isNotEmpty() &&
        account.user[0].userName.isNotBlank() &&
        account.user[0].email.isNotBlank() &&
        account.user[0].pass.isNotBlank()) {

        // Check if the account for the given site already exists
        val existingAccount = listAcc.find { it.site == account.site }

        return if (existingAccount != null) {
            // If the account already exists, add the user object to its list of users
            val updatedUsers = existingAccount.user.toMutableList().apply { add(account.user[0]) }
            val updatedExistingAccount = existingAccount.copy(user = updatedUsers)
            val updatedList = listAcc.toMutableList().apply {
                // Replace the existing account with the updated one
                set(indexOf(existingAccount), updatedExistingAccount)
            }
            updatedList
        } else {
            // If the account doesn't exist, add the account object to the list
            listAcc + account
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


private fun update_data_to_file( userList:List<Account>,context: Context){
    val fileName = "data.json"
    val baseFileName = "data"
    var index = 1
    var dataFile: File
    val downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    if (downloadDir.exists() && downloadDir.isDirectory) {
        val jsonDataDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "DataBase"
        )
        if (jsonDataDir.exists() && jsonDataDir.isDirectory) {
            val file = File(jsonDataDir,fileName)
            if(file.exists()){
                val jsonString = Gson().toJson(userList)
                val data01=File(jsonDataDir,"data01.json")
                if(file.canRead()||file.canWrite()){
                    file.writeText(jsonString)
                }else{
                    if(data01.exists()){
                        do {
                            val fileName2 = "$baseFileName${String.format("%02d", index)}.json"
                            dataFile = File(jsonDataDir, fileName2)
                            index++
                        } while (dataFile.exists())
                        try {
                            dataFile.writeText(jsonString)
                            saveDataName(context,"jsonFileName",dataFile.name)

                        } catch (e: Exception) {
                            Log.e("MainActivity","${e.message}")
                        }
                    }else{
                        dataFile=File(jsonDataDir,"data01.json")
                        try {
                            dataFile.writeText(jsonString)
                            saveDataName(context,"jsonFileName",dataFile.name)

                        } catch (e: Exception) {
                            Log.e("MainActivity","${e.message}")
                        }

                    }
                }

//             Convert the list of User objects to a JSON string using Gson
            }else{
                try {
                    // Convert the list of User objects to a JSON string using Gson
                    val jsonString = Gson().toJson(userList)
                    // Write the JSON string to a file
                    val file = File(jsonDataDir, fileName)
                    try {
                        file.writeText(jsonString)
                        saveDataName(context,"jsonFileName",fileName)
                    } catch (e: Exception) {
                        Log.e("MainActivity","${e.message}")
                    }

                    Log.e("MainActivity","data.json created")

                } catch (e: Exception) {
                    Log.e("MainActivity", "Error creating JSON file: ${e.message}")
                }
            }

        }
    }


}

fun saveDataName(context: Context, newKey: String, value: String) {
    val sharedPreferences = context.getSharedPreferences("dataFileName", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString(newKey, value) // Use the new key to save the value
    editor.apply()
}

// Function to retrieve a string value from SharedPreferences using a custom key
fun getDataName(context: Context, key: String): String? {
    val sharedPreferences = context.getSharedPreferences("dataFileName", Context.MODE_PRIVATE)
    return sharedPreferences.getString(key, null)
}


@Composable
fun CategoryItems(
    title: String,
    onSelect: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onSelect(title)
            }
            .padding(10.dp)
    ) {
        Text(text = title, fontSize = 16.sp)
    }

}

