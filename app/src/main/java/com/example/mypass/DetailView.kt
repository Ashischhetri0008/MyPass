package com.example.mypass

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.mypass.InputsUI.IconAndText
import com.example.mypass.schema.Account
import com.example.mypass.sharedViewModel.SharedViewModel
import com.google.gson.Gson


@Composable
fun DetailsView(index: Int?,viewModel: SharedViewModel) {
    Surface(modifier = Modifier
        .fillMaxSize()
    ) {
        val jsonString = viewModel.navigationArguments.value
        val listAcc = Gson().fromJson(jsonString, Array<Account>::class.java).toList()
        val account = listAcc[index!!]
        if(account.users.isNotEmpty()){
            val listUsers = account.users
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 12.dp, top = 12.dp, end = 12.dp, bottom = 0.dp)
            ) {

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(listUsers) { users ->
                        DetailsCard(users.userName, users.email, users.password)
                    }
                }
            }
        }else{
            Column(modifier = Modifier
                .fillMaxSize(), // Fill the available space
                verticalArrangement = Arrangement.Center, // Center vertically
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                SampleText(text = "No User ID")
            }
        }
    }
}

@Composable
fun DetailsCard(userName:String,email: String,pass: String, modifier: Modifier = Modifier) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 0.dp, vertical = 0.dp),
        shape = RoundedCornerShape(15.dp)
    ){
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)) {
            IconAndText(icon = Icons.Outlined.AccountCircle, iconDes = "User", text = userName)
            IconAndText(icon = Icons.Outlined.Mail, iconDes = "Email", text = email)
            IconAndText(icon = Icons.Outlined.Lock, iconDes = "Password", text = pass)
        }
    }
}


// Show Toast Meassage
@Composable
private fun showToast(message: String) {
    val context= LocalContext.current
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}
