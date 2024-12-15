package com.example.myapplication.assignment.presentation.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.AlertDialog
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.assignment.presentation.viewmodel.TodoViewModel

@Composable
fun AddTodoItem(
    viewmodel: TodoViewModel = viewModel(),
    navigateToListScreen: (String) -> Unit
) {
    var todoItem by remember { mutableStateOf(TextFieldValue()) }
    val todoUiState = viewmodel.addState.collectAsState()

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
                    when (val state = todoUiState.value) {
                        is TodoViewModel.AddUiState.Loading -> {
                            ShowProgress(true)
                        }

                        is TodoViewModel.AddUiState.Error -> {
                            CreatePopupScreen(state.message)
                        }

                        is TodoViewModel.AddUiState.Exception -> {
                            navigateToListScreen(state.message)
                            viewmodel.resetStateValue()

                        }

                        is TodoViewModel.AddUiState.NavigateToListScreen -> {
                            navigateToListScreen("")
                            viewmodel.resetStateValue()
                        }

                        TodoViewModel.AddUiState.Nothing -> {}
                    }

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

@Composable
fun ShowProgress(showProgress: Boolean) {
    if (showProgress) {
        Box(
            modifier = Modifier.wrapContentSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun CreatePopupScreen(result: String) {
    Toast.makeText(LocalContext.current, result, Toast.LENGTH_SHORT).show()
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ShowProgress(true)
}