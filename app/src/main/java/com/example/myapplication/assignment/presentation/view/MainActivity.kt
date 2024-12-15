package com.example.myapplication.assignment.presentation.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.assignment.presentation.viewmodel.TodoViewModel
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

    NavHost(navController = navController, startDestination = "list_screen") {
        composable("list_screen") {
            // get the Error message from the Add screen to display in the list screen
            val message = it.savedStateHandle.get<String>("result") ?: ""
            // Setting the value as null
            navController.previousBackStackEntry?.savedStateHandle?.set("result", null)
            // Navigating to list screen
            TodoListActivity(navController, viewmodel, message)
        }

        // navigating to details screen
        composable("add_screen") {
            AddTodoItem(
                navigateToListScreen = navController::navigateToListScreen,
                viewmodel = viewmodel
            )
        }
    }
}

fun NavController.navigateToListScreen(message: String) {
    if (message.isNotEmpty()) {
        val previousBackStackEntry = this.previousBackStackEntry
        previousBackStackEntry?.savedStateHandle?.set("result", message)
    }
    this.popBackStack()
}