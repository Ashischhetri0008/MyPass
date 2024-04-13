package com.example.mypass

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mypass.schema.Account
import com.example.mypass.sharedViewModel.SharedViewModel
import com.example.mypass.sharedViewModel.iconList
import com.example.mypass.sharedViewModel.site_icon
import com.google.gson.Gson
import kotlin.math.round
import kotlin.reflect.typeOf

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


@OptIn(ExperimentalMaterial3Api::class)
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
            var filteredAccounts = listAcc.filter { account ->
                account.tags.contains(selectedTag.value)
            }
            Column {
                var text by remember {
                    mutableStateOf("")
                }
                var active by remember {
                    mutableStateOf(false)
                }
                val focusManager = LocalFocusManager.current
                // Text Field Search
                TextField(modifier = Modifier.fillMaxWidth(),
                    value = text,
                    onValueChange = {
                        text = it
                        active = true
                    },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    placeholder = { Text("Search") },
                    trailingIcon = {
                        if(active){
                            IconButton(onClick = { if(text.isNotEmpty()) text = "" else {
                                focusManager.clearFocus()
                                active= !active
                            }
                            }) {
                                Icon(Icons.Default.Close, contentDescription = null)
                            }
                        } }
                    ,
                )
                if(text.isNotEmpty()){
                    filteredAccounts=listAcc.filter { account ->
                        account.head.lowercase().contains(text.lowercase().trim())
                    }
                }

                Row(modifier = Modifier
                    .padding(start = 10.dp, top = 5.dp, end = 10.dp, bottom = 5.dp),
                ){
                    LazyRow( modifier = Modifier
                        .fillMaxWidth()
                        .padding(innerPadding),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {

                        // Add the special item first if it matches "all" or "most"
                        if (distinctTags.contains("most")) {
                            item {
                                val isSelected = "most" == selectedTag.value
                                Tags(name = "most",isSelected = isSelected) {
                                    selectedTag.value = "most"
                                }
                            }
                        }
                        if (distinctTags.contains("all")) {
                            item {
                                val isSelected = "all" == selectedTag.value
                                Tags(name = "all",isSelected = isSelected) {
                                    selectedTag.value = "all"
                                }
                            }
                        }


                        // Add other items after the special items
                        distinctTags.filter { it != "all" && it != "most" }.forEach { item ->
                            item {
                                val isSelected = item == selectedTag.value
                                Tags(name = item,isSelected = isSelected) {
                                    selectedTag.value = it
                                }
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
        Row(modifier = Modifier

            .padding(10.dp)
        ) {
            var foundIcon = iconList().find { it.name == site.lowercase()}

           if(foundIcon!=null){
                Image(
                    painterResource(id = foundIcon.icon),
                    contentDescription = stringResource(id = R.string.account_icon),
                    modifier = Modifier
                        .size(45.dp)
                        .align(Alignment.CenterVertically)
                )
           }else{
               Image(
                   painterResource(id = R.drawable.other),
                   contentDescription = stringResource(id = R.string.account_icon),
                   modifier = Modifier
                       .size(45.dp)
                       .align(Alignment.CenterVertically)
               )
           }

            Text(modifier = Modifier
                .padding(10.dp)
                .align(Alignment.CenterVertically),
                fontSize = 24.sp,
                text = site.lowercase().replaceFirstChar { it.uppercase() })
        }

    }
}
@Composable
fun Tags(name: String,isSelected: Boolean, onClick: (String) -> Unit) {
    Surface(
        modifier = Modifier
            .clickable{
            onClick(name)
        },

        color = if (isSelected) Color.Black else Color.LightGray,
        shape = RoundedCornerShape(6.dp)
    ) {
        Text(
            text = name,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            color = if (isSelected) Color.White else Color.Black,
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
