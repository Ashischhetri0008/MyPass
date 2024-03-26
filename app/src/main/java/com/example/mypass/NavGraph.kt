package com.example.mypass

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.mypass.InputsUI.AddAccountsDetails
import com.example.mypass.sharedViewModel.SharedViewModel

@Composable
fun SetupNavGraph(
    navController: NavHostController
){
    val viewModel: SharedViewModel = viewModel()
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(navController = navController,viewModel)
        }
        composable(Screen.Detail.route,
            arguments = listOf(
                navArgument("itemId"){
                    type = NavType.IntType
                }
            )
        ) {
                backStackEntry ->
            val itemId = backStackEntry.arguments?.getInt("itemId")
            if (itemId != null) {
                DetailsView(itemId = itemId)
            } else {
                // Handle error or fallback behavior
            }

        }
        composable(Screen.AddData.route, arguments = listOf(
            navArgument("jsonString"){
                type = NavType.StringType
            }
        )){
            val jsonString=it.arguments!!.getString("jsonString")
            AddAccountsDetails(jsonString!!,viewModel,navController)
        }
    }
}