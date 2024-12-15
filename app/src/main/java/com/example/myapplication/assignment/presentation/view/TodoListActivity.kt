package com.example.myapplication.assignment.presentation.view

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import com.example.myapplication.assignment.domain.TodoItem
import com.example.myapplication.assignment.presentation.viewmodel.TodoViewModel
import com.example.myapplication.assignment.utils.ObserverLifeCycleEvents

@Composable
fun TodoListActivity(navController: NavHostController, viewmodel: TodoViewModel) {
    val todoUiState = viewmodel.state.collectAsState()

    viewmodel.ObserverLifeCycleEvents(LocalLifecycleOwner.current.lifecycle)

    when (val state = todoUiState.value) {
        is TodoViewModel.UiState.Loading -> {
            CircularProgressIndicator()
        }

        is TodoViewModel.UiState.Success -> {

            AddScreen(
                state.data,
                viewmodel,
                navController
            )
        }
    }


}

@Composable
fun AddScreen(
    todoUiState: List<TodoItem>,
    viewmodel: TodoViewModel,
    navController: NavHostController
) {

    var searchQuery by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue("Initial Text"))
    }

    var isSearchBarVisible by rememberSaveable { mutableStateOf(false) }

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

                        LazyColumn(Modifier.fillMaxWidth()) {
                            items(todoUiState) { item ->
                                TodoListItem(todoItem = item)
                            }
                        }

                    }
                }
            }
        )


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
