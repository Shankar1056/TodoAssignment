package com.example.myapplication.assignment.presentation.viewmodel

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.assignment.domain.TodoItem
import com.example.myapplication.assignment.domain.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val repository: TodoRepository
) : ViewModel(), DefaultLifecycleObserver {

    private val _state = MutableStateFlow<UiState>(UiState.Loading)
    val state = _state.asStateFlow()

    private val _filteredState = MutableStateFlow<List<TodoItem>>(listOf(TodoItem()))
    val filteredState = _filteredState.asStateFlow()

    private val _addstate = MutableStateFlow<AddUiState>(AddUiState.Nothing)
    val addState = _addstate.asStateFlow()

    fun resetStateValue() {
        _addstate.value = AddUiState.Nothing
    }

    fun saveTodoItem(item: String) {
        if (item.isEmpty()) {
            _addstate.value = AddUiState.Error("There is nothing to add")
        } else if (item == "Error") {
            _addstate.value = AddUiState.Exception("Failed to add TODO")
        } else {
            val todoItem = TodoItem(item)
            viewModelScope.launch {
                _addstate.value = AddUiState.Loading
                delay(3000)
                repository.saveItem(todoItem)
                _addstate.value = AddUiState.NavigateToListScreen
            }
        }
    }

    fun getUserDetails() {
        viewModelScope.launch {
            val result = repository.getAllItems()
            _state.value = UiState.Success(result)
            _filteredState.value = result

        }
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        getUserDetails()

    }

    fun onSearchQueryChanged(query: String) {
        filterList(query)
    }

    private fun filterList(query: String) {
        _state.value = UiState.Success(filteredState.value)
        val data = (state.value as UiState.Success).data
        _state.value = UiState.Success(data.filter { it.item.contains(query, ignoreCase = true) })
    }

    // this UiState created for List Screen
    sealed class UiState {
        object Loading : UiState()
        data class Success(val data: List<TodoItem>) : UiState()
    }

    // this AddUiState created for Add Screen
    sealed class AddUiState {
        object Nothing : AddUiState()
        object Loading : AddUiState()
        data class Error(val message: String) : AddUiState()
        data class Exception(val message: String) : AddUiState()
        object NavigateToListScreen : AddUiState()
    }

}

