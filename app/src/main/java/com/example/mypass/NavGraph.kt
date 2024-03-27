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
    navController: NavHostController,
    viewModel: SharedViewModel
){
//    val viewModel: SharedViewModel = viewModel()
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(navController = navController,viewModel)
        }
        composable(Screen.Detail.route,
            arguments = listOf(
                navArgument("Index"){
                    type = NavType.IntType
                }
            )
        ) {
                backStackEntry ->
            val index = backStackEntry.arguments?.getInt("Index")
            if (index != null) {
                DetailsView(index = index,viewModel)
            } else {
                // Handle error or fallback behavior
            }

        }
        composable(Screen.AddData.route
        ){
            AddAccountsDetails(viewModel,navController)
        }
    }
}