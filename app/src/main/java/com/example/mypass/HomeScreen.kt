package com.example.mypass

import android.content.Context
import android.util.Log
import android.widget.Toast

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mypass.schema.Account
import com.example.mypass.sharedViewModel.SharedViewModel
import com.google.gson.Gson

@Composable
fun HomeScreen(navController: NavHostController,viewModel: SharedViewModel){
    Surface(modifier = Modifier
        .fillMaxSize()
        .padding(start = 12.dp, top = 12.dp, end = 12.dp, bottom = 0.dp),
    ){
        val context= LocalContext.current
        var listAcc:List<Account> = remember {
            mutableStateListOf()
        }
        try{
            val jsonString=viewModel.navigationArguments.value
            listAcc=Gson().fromJson(jsonString, Array<Account>::class.java).toList()
        }catch (e:Exception){
            Log.e("MianActivity","${e.message}")
        }
        if(listAcc.isEmpty()){
            showToast(context,"DB is empty")
            navController.navigate(Screen.AddData.route)
        }
        Scaffold(listAcc,navController){}

    }
}


@Composable
fun Scaffold(listAcc:List<Account>, navController:NavHostController, function: () -> Unit) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 5.dp),
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(route = "addData")
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { innerPadding ->
        if(listAcc.isEmpty()){
            Column(modifier = Modifier
                .fillMaxSize(), // Fill the available space
            verticalArrangement = Arrangement.Center, // Center vertically
            horizontalAlignment = Alignment.CenterHorizontally
                ){
                SampleText(text = "No Accounts")
            }
        }else{
            val distinctTags = listAcc
                .flatMap { it.tags } // Flatten all tag lists into a single list
                .distinct() // Get distinct tag values
                .sorted()
            val selectedTag = remember { mutableStateOf<String?>("most") }

            // Filtered list of accounts based on the selected tag
            val filteredAccounts = listAcc.filter { account ->
                account.tags.contains(selectedTag.value)
            }

            Column {
                Row(modifier = Modifier
                    .padding(start = 10.dp, top = 5.dp, end = 10.dp, bottom = 5.dp),
                ){
                    LazyRow( modifier = Modifier
                        .fillMaxWidth()
                        .padding(innerPadding),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                        items(distinctTags){item->
                            Tags(name = item) {
                                selectedTag.value= it
                            }
                        }
                    }
                }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                ) {
                    items(filteredAccounts) { item ->
                        Account(listAcc.indexOf(item),item.head, navController)
                    }
                }
            }
        }
    }
}


@Composable
fun Account(index: Int,site: String,navController: NavHostController){
    val context= LocalContext.current
    ElevatedCard(modifier = Modifier
        .fillMaxWidth()
        .clickable {
            navController.navigate(route = "detail/$index")
        }
        .padding(start = 10.dp, top = 5.dp, end = 10.dp, bottom = 5.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
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

@Composable
fun SampleText(text: String){
    Text(text = text)
}
private fun showToast(context: Context,message: String) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}
