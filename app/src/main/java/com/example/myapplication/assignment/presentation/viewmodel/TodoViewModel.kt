package com.example.myapplication.assignment.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.R
import com.example.myapplication.assignment.domain.TodoItem
import com.example.myapplication.assignment.domain.TodoRepository
import com.example.myapplication.assignment.utils.UtilCons
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val repository: TodoRepository,
    @ApplicationContext private val context: Context
) : ViewModel(), DefaultLifecycleObserver {

    private val _state = MutableStateFlow<UiState>(UiState.Loading)
    val state = _state.asStateFlow()

    private val _filteredState = MutableStateFlow(listOf(TodoItem()))
    val filteredState = _filteredState.asStateFlow()

    private val _addState = MutableStateFlow<AddUiState>(AddUiState.Nothing)
    val addState = _addState.asStateFlow()


    fun resetStateValue() {
        _addState.value = AddUiState.Nothing
    }

    fun saveTodoItem(item: String) {
        if (item.isEmpty()) {
            _addState.value = AddUiState.Error(context.getString(R.string.empty_text_add))
        } else if (item == UtilCons.ERROR) {
            _addState.value = AddUiState.Exception(context.getString(R.string.fail_add_message))
        } else {
            val todoItem = TodoItem(item)
            viewModelScope.launch {
                _addState.value = AddUiState.Loading
                delay(3000)
                repository.saveItem(todoItem)
                _addState.value = AddUiState.NavigateToListScreen
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

