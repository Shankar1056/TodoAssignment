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
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.assignment.presentation.viewmodel.TodoViewModel

@Composable
fun AddTodoItem(
    viewmodel: TodoViewModel = viewModel(),
    navigateToListScreen: (String) -> Unit,
    navigateToBack: () -> Unit
) {
    var todoItem by remember { mutableStateOf(TextFieldValue()) }
    val todoUiState = viewmodel.addState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.app_name))
                },
                navigationIcon = {
                    IconButton(onClick = { navigateToBack.invoke() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                elevation = dimensionResource(R.dimen.top_bar_elevation)
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
                        .padding(dimensionResource(R.dimen.fab_margin))
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
                            navigateToListScreen(stringResource(R.string.empty_string))
                            viewmodel.resetStateValue()
                        }

                        TodoViewModel.AddUiState.Nothing -> {}
                    }

                    TextField(
                        value = todoItem,
                        onValueChange = { todoItem = it },
                        label = { Text(stringResource(R.string.add_todo)) },
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
                            .padding(dimensionResource(R.dimen.dimen_30))
                    ) {
                        Text(stringResource(R.string.add_todo))
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