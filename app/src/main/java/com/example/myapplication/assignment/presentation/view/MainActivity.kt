package com.example.myapplication.assignment.presentation.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.R
import com.example.myapplication.assignment.presentation.viewmodel.TodoViewModel
import com.example.myapplication.assignment.utils.UtilCons
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavigation()
        }

    }

}

@Composable
fun AppNavigation() {
    val viewmodel: TodoViewModel = hiltViewModel()
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = UtilCons.LIST_SCREEN) {
        composable(UtilCons.LIST_SCREEN) {
            val message = it.savedStateHandle.get<String>(UtilCons.RESULT_EXTRA) ?: stringResource(R.string.empty_string)
            navController.previousBackStackEntry?.savedStateHandle?.set(UtilCons.RESULT_EXTRA, null)
            TodoListActivity(
                navigateToAddScreen = {
                    navController.navigate(UtilCons.ADD_SCREEN)
                },
                viewmodel,
                message
            )
        }

        composable(UtilCons.ADD_SCREEN) {
            AddTodoItem(
                navigateToListScreen = navController::navigateToListScreen,
                viewmodel = viewmodel,
                navigateToBack = navController::navigateToBack
            )
        }
    }
}

fun NavController.navigateToListScreen(message: String) {
    if (message.isNotEmpty()) {
        val previousBackStackEntry = this.previousBackStackEntry
        previousBackStackEntry?.savedStateHandle?.set(UtilCons.RESULT_EXTRA, message)
    }
    this.popBackStack()
}

fun NavController.navigateToBack() {
    this.popBackStack()
}