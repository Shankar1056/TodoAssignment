package com.example.myapplication.assignment.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import com.example.myapplication.assignment.domain.TodoItem
import com.example.myapplication.assignment.presentation.viewmodel.TodoViewModel
import com.example.myapplication.assignment.utils.ObserverLifeCycleEvents

@Composable
fun TodoListActivity(navController: NavHostController, viewmodel: TodoViewModel, message: String) {

    /*val result =
        navController.previousBackStackEntry?.savedStateHandle?.get<String>("result")
    if (result != null) {
        CreatePopup(result)
    }*/

    CreateScreen(viewmodel, navController, message)

}

@Composable
fun CreateScreen(
    viewmodel: TodoViewModel,
    navController: NavHostController,
    message: String

) {

    var searchQuery by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue("Initial Text"))
    }

    viewmodel.ObserverLifeCycleEvents(LocalLifecycleOwner.current.lifecycle)

    var isSearchBarVisible by rememberSaveable { mutableStateOf(false) }
    val uiState = viewmodel.state.collectAsState()

    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        if (isSearchBarVisible) {
                            SearchTextField(
                                value = searchQuery,
                                onValueChange = {
                                    searchQuery = it
                                    viewmodel.onSearchQueryChanged(it.text)
                                },
                            )
                        } else {
                            Text(text = "Search List")
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            isSearchBarVisible = !isSearchBarVisible
                            searchQuery = searchQuery.copy("")
                        }) {
                            androidx.compose.material.Icon(
                                imageVector = if (isSearchBarVisible) Icons.Filled.Close else Icons.Filled.Search,
                                contentDescription = if (isSearchBarVisible) "Close Search" else "Open Search"
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    elevation = 4.dp
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { navController.navigate("add_screen") }) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            },
            content = { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize() // Ensures it takes up the entire screen
                        .padding(paddingValues)
                        .background(Color.White) // Set the background color to blue
                ) {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        when (val state = uiState.value) {
                            is TodoViewModel.UiState.Loading -> {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }

                            is TodoViewModel.UiState.Success -> {
                                if (state.data.isEmpty()) {
                                    CreateTextViewToAddItem(navController)
                                } else {
                                    LazyColumn(Modifier.fillMaxWidth()) {
                                        items(state.data) { item ->
                                            TodoListItem(todoItem = item)
                                        }
                                    }
                                }
                            }
                        }

                        if (message.isNotBlank()) {
                            CreatePopup(message)
                        }


                    }
                }
            }
        )
    }

}

@Composable
fun CreatePopup(result: String) {
    var showDialog by remember { mutableStateOf(true) }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { true },
            title = { Text(text = "Popup Title") },
            text = { Text(result) },
            confirmButton = {
                Button(
                    onClick = { showDialog = false }
                ) {
                    Text("OK")
                }
            },
            modifier = Modifier.padding(16.dp)
        )
    }
}


@Composable
fun CreateTextViewToAddItem(navController: NavHostController) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Button(
            onClick = { navController.navigate("add_screen") },
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text(
                text = "Press the + button to add a TODO item.",
                color = Color.White
            )
        }

    }
}


@Composable
fun SearchTextField(value: TextFieldValue, onValueChange: (TextFieldValue) -> Unit) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        textStyle = androidx.compose.material.MaterialTheme.typography.body1.copy(color = Color.White),
        singleLine = true,
        cursorBrush = SolidColor(Color.White),

        )
}


@Composable
fun TodoListItem(todoItem: TodoItem) {
    Text(
        text = todoItem.item,
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        color = Color.Black,
    )
}
