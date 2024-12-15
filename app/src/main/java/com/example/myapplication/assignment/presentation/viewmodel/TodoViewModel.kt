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

    private val _addstate = MutableStateFlow<AddUiState>(AddUiState.Loading)
    val addState = _addstate.asStateFlow()

    fun resetStateVakue() {
        _addstate.value = AddUiState.Loading
    }

    fun saveTodoItem(item: String) {
        val todoItem = TodoItem(item)
        viewModelScope.launch {
            _addstate.value = AddUiState.Loading

            repository.saveUser(todoItem)
            delay(3000)
            _addstate.value = AddUiState.NavigateToListScreen
        }
    }

    fun getUserDetails() {
        viewModelScope.launch {
            val result = repository.getAllUsers()
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

    sealed class UiState {
        object Loading : UiState()
        data class Success(val data: List<TodoItem>) : UiState()
    }

    sealed class AddUiState {
        object Loading : AddUiState()
        data class Error(val message: String) : AddUiState()
        object NavigateToListScreen : AddUiState()
    }

}

