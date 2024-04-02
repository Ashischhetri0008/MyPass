package com.example.mypass.InputsUI

import android.content.Context
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
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

// write json string to file
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
    var tfHead by remember { mutableStateOf("") }
    var tags by remember { mutableStateOf(listOf<String>()) }
    var tftag by remember { mutableStateOf("") }
    var tfuserName by remember { mutableStateOf("") }
    var tfemail by remember { mutableStateOf("") }
    var tfpass by remember { mutableStateOf("") }

    val context= LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 30.dp, top = 50.dp, end = 30.dp, bottom = 50.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            var textFieldWidth by remember { mutableStateOf(0) }

            // Head row
            Column(modifier = Modifier
                .fillMaxWidth(),
            ) {
                var expanded by remember {
                    mutableStateOf(false)
                }
                Row(modifier = Modifier
                    .fillMaxWidth(),
                ) {
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
                                } else {
                                    false
                                }
                            }
                            .clickable(
                                onClick = {
                                    expanded = false
                                }),
                        value = tfHead,
                        onValueChange = {
                            tfHead = it
                           expanded = true
                        },
                        singleLine = true,
                        leadingIcon = {
                            Icon(
                                Icons.Outlined.WebAsset,
                                contentDescription = "Head"
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { expanded = !expanded }) {
                                Icon(Icons.Outlined.ArrowDropDown, "Down arrow")
                            }
                        },
                        label = { Text("Head") }
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
                                    if (tfHead.isNotEmpty()) {
                                        items(
                                            updatedList.map { it.head }.filter {
                                                it.lowercase()
                                                    .contains(tfHead.lowercase())
                                            }
                                        ) {
                                            CategoryItems(title = it) { title ->
                                                tfHead = title
                                                tags = updatedList.find { it.head == title }?.tags!!
                                                expanded = false
                                            }
                                        }
                                    } else {
                                        items(
                                            updatedList.map { it.head }.sorted()
                                        ) {
                                            CategoryItems(title = it) { title ->
                                                tfHead = title
                                                tags = updatedList.find { it.head == title }?.tags!!
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

            // Tags
            Row(modifier = Modifier
                .padding(start = 10.dp, top = 5.dp, end = 10.dp, bottom = 5.dp),
            ){
                LazyRow( modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(tags){item->
                        Tags(name = item) {
                            try {
                                if(it!="all"){
                                    tags=tags-it
                                }
                            }catch (e:Exception){
                                showToast(context,"${e.message}")
                            }
                        }
                    }
                }
            }

            // add Tag row
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier
                    .fillMaxWidth(0.75f)
                ) {
                    var expanded by remember {
                        mutableStateOf(false)
                    }
                    Row(modifier = Modifier
                        .fillMaxWidth(),
                    ) {
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
                                    } else {
                                        false
                                    }
                                }
                                .clickable(
                                    onClick = {
                                        expanded = false
                                    }),
                            value = tftag,
                            onValueChange = {
                                tftag = it
                                expanded = false
                            },
                            singleLine = true,
                            leadingIcon = {
                                Icon(
                                    Icons.Outlined.WebAsset,
                                    contentDescription = "Tag"
                                )
                            },
                            trailingIcon = {
                                IconButton(onClick = { expanded = !expanded }) {
                                    Icon(Icons.Outlined.ArrowDropDown, "Down arrow")
                                }
                            },
                            label = { Text("Tag") }
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
                                        if (tftag.isNotEmpty()) {
                                            items(
                                                updatedList
                                                    .flatMap { it.tags } // Flatten all tag lists into a single list
                                                    .distinct() // Get distinct tag values
                                                    .filter {
                                                        it.lowercase()
                                                            .contains(tftag.lowercase())
                                                    }
                                            ) {
                                                CategoryItems(title = it) { title ->
                                                    tftag = title
                                                    expanded = false
                                                }
                                            }

                                        } else {
                                            items(
                                                updatedList
                                                    .flatMap { it.tags } // Flatten all tag lists into a single list
                                                    .distinct().sorted() // Get distinct tag values
                                            ) {
                                                CategoryItems(title = it) { title ->
                                                    tftag = title
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
                Spacer(modifier = Modifier.width(10.dp))
                Button(
                    onClick =
                    {
                        if(!tags.contains(tftag.trim())){
                            tags=tags + tftag.trim()
                        }
                    },
                ) {}
            }

            // User Name row
            Row(modifier = Modifier.fillMaxWidth()) {
                // Text field for user name
                OutlinedTextField(modifier = Modifier.fillMaxWidth(),
                    value = tfuserName,
                    onValueChange = { tfuserName = it },
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
            Column(modifier = Modifier
                .fillMaxWidth(),
            ) {
                var expanded by remember {
                    mutableStateOf(false)
                }
                Row(modifier = Modifier
                    .fillMaxWidth(),
                ) {
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
                                } else {
                                    false
                                }
                            }
                            .clickable(
                                onClick = {
                                    expanded = false
                                }),
                        value = tfemail,
                        onValueChange = {
                            tfemail = it
                            expanded = true
                        },
                        singleLine = true,
                        leadingIcon = {
                            Icon(
                                Icons.Outlined.Mail,
                                contentDescription = "Email"
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { expanded = !expanded }) {
                                Icon(Icons.Outlined.ArrowDropDown, "Down arrow")
                            }
                        },
                        label = { Text("Email") }
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
                                    if(tfHead.isNotBlank()){
                                        val filteredAccounts= updatedList.find { it.head.equals(tfHead, ignoreCase = true) }
                                        val users = filteredAccounts?.users
                                        if (users != null) {
                                            items(
                                                users.map { it.email }.filter {
                                                    it.lowercase()
                                                        .contains(tfemail.lowercase())
                                                }
                                            ) {
                                                CategoryItems(title = it) { title ->
                                                    tfemail = title
                                                    tfuserName= users.find { it.email == title }?.userName.toString()
                                                    tfpass = users.find { it.email == title }?.password.toString()
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

            }

            // Password row
            Row(modifier = Modifier.fillMaxWidth()) {
                // Text field for password
                OutlinedTextField(modifier = Modifier.fillMaxWidth(),
                    value = tfpass,
                    onValueChange = { tfpass = it },
                    label = { Text("Password") },
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.Lock,
                            contentDescription = "Password"
                        )
                    }
                )
            }
            Row(modifier = Modifier
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                // Button to submit the form
                Button(
                    onClick = {
                        if(tfuserName.isBlank() && tfemail.isBlank()){

                            val newAccount = Account(
                                tfHead.trim(),
                                tags,
                                emptyList()
                            )
                            updatedList = add_account_to_list(context,updatedList, newAccount)
                            navController.navigate("home")
                        }else{
                            val newAccount = Account(
                                tfHead.trim(),
                                tags,
                                listOf(
                                    Account.User(tfuserName.trim(),tfHead.trim(), tfemail.trim(), tfpass.trim())
                                )
                            )
                            updatedList = add_account_to_list(context,updatedList, newAccount)
                            navController.navigate("home")
                        }

                    }) {
                    Text(text = "Submit")
                }
            }
//            if(updatedList.isNotEmpty()){
//                Tags(name = updatedList[0].toString()) {
//
//                }
//            }

        }

    }

    return updatedList
}


private fun add_account_to_list(context: Context,listAcc: List<Account>, account: Account): List<Account>{

    if (account.head.isNotBlank()) {
        // Check if the account for the given site already exists
        val existingAccount = listAcc.find { it.head == account.head }

        if (existingAccount != null) {
            showToast(context,"ext: ${existingAccount.head}")
            if (account.tags!=existingAccount.tags) {
                existingAccount.tags=account.tags

            }
            if(account.users.isNotEmpty()){
                //            if(existingAccount.users.find { it.email == account.users[0].email }?.email.toString()== account.users[0].email){
                val existingUser = existingAccount.users.find { it.email == account.users[0].email }
                if (existingUser != null) {
                    // Update user data
                    val updatedUsers = existingAccount.users.map { if (it.email == account.users[0].email) account.users[0] else it }
                    val updatedExistingAccount = existingAccount.copy(users = updatedUsers)
                    val updatedList = listAcc.toMutableList().apply {
                        // Replace the existing account with the updated one
                        set(indexOf(existingAccount), updatedExistingAccount)
                    }
                    return updatedList
                } else {
                    // Add new user to the list of users
                    val updatedUsers = existingAccount.users.toMutableList().apply { add(account.users[0]) }
                    val updatedExistingAccount = existingAccount.copy(users = updatedUsers)
                    val updatedList = listAcc.toMutableList().apply {
                        // Replace the existing account with the updated one
                        set(indexOf(existingAccount), updatedExistingAccount)
                    }
                   return updatedList
                }
            }else{
                val updatedList = listAcc.toMutableList().apply {
                    // Replace the existing account with the updated one
                    set(indexOf(existingAccount), existingAccount)
                }
                updatedList
            }

        } else {

            account.tags +="all"
            // If the account doesn't exist, add the account object to the list
            val updatedList = listAcc.toMutableList().apply { add(account) }
            return updatedList

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
@Composable
fun Tags(name: String, onClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .clickable { onClick(name) }
    ) {
        Text(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 0.dp),
            textAlign = TextAlign.Center,
            text = name
        )
    }
}
private fun showToast(context: Context,message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

