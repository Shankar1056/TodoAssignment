package com.example.myapplication.assignment.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.myapplication.assignment.presentation.viewmodel.TodoViewModel

@Composable
fun AddTodoItem(navController: NavHostController, viewmodel: TodoViewModel) {
    var todoItem by remember { mutableStateOf(TextFieldValue()) }
    val todoUiState = viewmodel.addState.collectAsState()
    when (val state = todoUiState.value) {
        is TodoViewModel.AddUiState.Loading -> {
            CircularProgressIndicator()
        }

        is TodoViewModel.AddUiState.Error -> {


        }

        is TodoViewModel.AddUiState.NavigateToListScreen -> {
            if (navController.previousBackStackEntry != null) {
                navController.popBackStack()
                viewmodel.resetStateVakue()
            }
            /*navController.currentBackStackEntry?.savedStateHandle?.getStateFlow<String?>("newItem", null)?.collectAsState(null)?.value?.let { newItem ->
                if (newItem != null) {
                   // toDoItems = toDoItems + newItem
                    navController.currentBackStackEntry?.savedStateHandle?.remove<String>("newItem")
                }
            }
*/
        }

    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "My App")
                },
                modifier = Modifier.fillMaxWidth(),
                elevation = 4.dp
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color.White)
            ) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    TextField(
                        value = todoItem,
                        onValueChange = { todoItem = it },
                        label = { Text("Add TODO") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(paddingValues)
                    )

                    Button(
                        onClick = {
                            viewmodel.saveTodoItem(todoItem.text)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(30.dp)
                    ) {
                        Text("Add TODO")
                    }
                }
            }

        }
    )
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {

}