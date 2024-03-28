package com.example.mypass.InputsUI

import android.content.Context
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material.icons.outlined.WebAsset
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.navigation.NavHostController
import com.example.mypass.schema.Account
import com.example.mypass.sharedViewModel.SharedViewModel
import com.google.gson.Gson
import java.io.File


@Composable
fun AddAccountsDetails(viewModel: SharedViewModel,navController: NavHostController){
    Surface(modifier = Modifier
        .fillMaxSize())
    {
        val context= LocalContext.current
        var listAcc by remember {
            mutableStateOf(emptyList<Account>())
        }
        listAcc= parsString(viewModel.navigationArguments.value)
        val updatedListAcc= add_data_form(listAcc,navController)
        if(listAcc!=updatedListAcc){
            try{
                viewModel.updateNavigationArguments(Gson().toJson(updatedListAcc))
                updateDataToFile(viewModel,context)
            }catch (e:Exception){
                Log.e("MainActivity","file not updated: ${e.message}")
            }
        }
    }
}

fun updateDataToFile(viewModel: SharedViewModel, context: Context){
    val downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    if (downloadDir.exists() && downloadDir.isDirectory) {
        val jsonDataDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "DataBase"
        )
        if (jsonDataDir.exists() && jsonDataDir.isDirectory) {
            val file=File(jsonDataDir,getDataFileName(context,"jsonFileName").toString())
            if(file.exists()&&file.canRead()&&file.canWrite()){
                file.writeText(viewModel.navigationArguments.value)
            }else{
                showToast(context,"file not exist")
            }
        }
    }
}

@Composable
fun add_data_form(listAcc2: List<Account>, navController: NavHostController): List<Account> {
    var updatedList by remember { mutableStateOf(listAcc2) }

    // State variables for managing input values
    var site by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }

    var expanded by remember {
        mutableStateOf(false)
    }
    val context= LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 30.dp, top = 50.dp, end = 30.dp, bottom = 50.dp)
    ) {
        Column(modifier = Modifier
            .height(400.dp),
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            var textFieldWidth by remember { mutableStateOf(0) }
            Column(modifier = Modifier
                .fillMaxWidth(),
            ) {
                Row(modifier = Modifier
                    .fillMaxWidth(),
                ) {
                    val temp=
                    // OutlinedTextField
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .onGloballyPositioned { coordinates ->
                                textFieldWidth = coordinates.size.width
                            }
                            .onFocusEvent { focusState ->
                                expanded = if (focusState.isFocused) {
                                    false
                                }else{
                                    false
                                }
                            }
                            .clickable(
                            onClick = {
                                expanded = false
                            }),
                        value = site,
                        onValueChange = {
                            site = it
                           expanded = true
                        },
                        singleLine = true,
                        leadingIcon = {
                            Icon(
                                Icons.Outlined.WebAsset,
                                contentDescription = "Site"
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { expanded = !expanded }) {
                                Icon(Icons.Outlined.ArrowDropDown, "Down arrow")
                            }
                        },
                        label = { Text("Site") }
                    )
                }
                // Popup
                Box(
                    modifier = Modifier.width(textFieldWidth.dp)
                ) {
                    Popup() {
                        androidx.compose.animation.AnimatedVisibility(visible = expanded) {
                            Card(
                                modifier = Modifier
                                    .padding(horizontal = 30.dp)
                                    .width(textFieldWidth.dp),
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

            }


            Row(modifier = Modifier.fillMaxWidth()) {
                // Text field for user name
                OutlinedTextField(modifier = Modifier.fillMaxWidth(),
                    value = userName,
                    onValueChange = { userName = it },
                    label = { Text("User Name") },
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.AccountCircle,
                            contentDescription = "User"
                        )
                    }
                )

            }
            // Email field
            Row(modifier = Modifier.fillMaxWidth()) {
                // Text field for email
                OutlinedTextField(modifier = Modifier.fillMaxWidth(),
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    leadingIcon = {
                        Icon(Icons.Outlined.Mail, contentDescription = "Email")
                    }
                )

            }
            Row(modifier = Modifier.fillMaxWidth()) {
                // Text field for password
                OutlinedTextField(modifier = Modifier.fillMaxWidth(),
                    value = pass,
                    onValueChange = { pass = it },
                    label = { Text("Password") },
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.Lock,
                            contentDescription = "Password"
                        )
                    }
                )
            }
        }
        Row(modifier = Modifier
            .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
        ) {
            // Button to submit the form
            Button(
                onClick = {
                val newAccount = Account(
                    site.trim(),
                    listOf(
                        Account.User(site.trim(),userName.trim(), email.trim(), pass.trim())
                    )
                )
                updatedList = add_account_to_list(updatedList, newAccount)
                navController.navigate("home")
            }) {
                Text(text = "Submit")
            }
        }
    }

    return updatedList
}


private fun add_account_to_list(listAcc: List<Account>, account: Account): List<Account>{
    if (account.site.isNotBlank() &&
        account.users.isNotEmpty() &&
        account.users[0].userName.isNotBlank() &&
        account.users[0].webSite.isNotBlank() &&
        account.users[0].email.isNotBlank() &&
        account.users[0].password.isNotBlank()) {

        // Check if the account for the given site already exists
        val existingAccount = listAcc.find { it.site == account.site }

        return if (existingAccount != null) {
            // If the account already exists, add the user object to its list of users
            val updatedUsers = existingAccount.users.toMutableList().apply { add(account.users[0]) }
            val updatedExistingAccount = existingAccount.copy(users = updatedUsers)
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
private fun showToast(context: Context,message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

