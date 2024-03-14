package com.example.mypass

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun HomeScreen(navController: NavHostController){
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(start = 12.dp, top = 12.dp, end = 12.dp, bottom = 0.dp)
    ){
        Column (verticalArrangement = Arrangement.spacedBy(12.dp)){
            Account(1,"facebook",navController)
            Account(2,"instagram",navController)
            Account(3,"google",navController)
            Account(4,"games",navController)
        }
    }
}

@Composable
fun Account(itemId: Int, site: String,navController: NavHostController){
    Card(modifier = Modifier
        .fillMaxWidth()
        .clickable {navController.navigate(route = "detail/$itemId")}
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