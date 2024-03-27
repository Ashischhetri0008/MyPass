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
import com.example.mypass.InputsUI.getDataFileName
import com.example.mypass.schema.Account
import com.example.mypass.sharedViewModel.SharedViewModel
import com.google.gson.Gson
import java.io.File


@Composable
fun DetailsView(index: Int?,viewModel: SharedViewModel) {

    if (viewModel.navigationArguments.value.isBlank()) {

    } else {
        val jsonString = viewModel.navigationArguments.value
        val listAcc = Gson().fromJson(jsonString, Array<Account>::class.java).toList()
        val exaccount = listAcc[index!!]
        if (exaccount != null) {
            val listUsers = exaccount.users
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
            SampleText(text = "No User ID")
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
