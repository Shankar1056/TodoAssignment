package com.example.myapplication.viewmodel

import com.example.myapplication.assignment.domain.TodoItem
import com.example.myapplication.assignment.domain.TodoRepository
import com.example.myapplication.assignment.presentation.viewmodel.TodoViewModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TodoViewModelTest {
    private lateinit var viewModel: TodoViewModel
    private val repository: TodoRepository = mockk(relaxed = true)

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        viewModel = TodoViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Given item is present, When saveTodoItem called, Then verify the state`() = runTest {
        // Given
        val todoItem = "Dummy Test"
        coEvery { repository.saveItem(any()) } returns Unit

        // When
        viewModel.saveTodoItem(todoItem)
        advanceUntilIdle()

        // Then
        TestCase.assertEquals(
            viewModel.addState.value,
            TodoViewModel.AddUiState.NavigateToListScreen
        )
    }

    @Test
    fun `Given item is present, When getUserDetails called, Then verify the state`() = runTest {
        // Given
        val todoItemList = listOf(
            TodoItem("Dummy Item")
        )
        coEvery { repository.getAllItems() } returns todoItemList

        // When
        viewModel.getUserDetails()
        advanceUntilIdle()

        // Then
        TestCase.assertEquals(
            viewModel.state.value,
            TodoViewModel.UiState.Success(todoItemList)
        )
    }

    @Test
    fun `Given item is present, When onSearchQueryChanged called, Then verify the state`() = runTest {
        // Given
        val todoItemList = emptyList<TodoItem>()

        // When
        viewModel.onSearchQueryChanged("Dummy")

        // Then
        TestCase.assertEquals(
            viewModel.state.value,
            TodoViewModel.UiState.Success(todoItemList)
        )
    }

    @Test
    fun `Given viewmodel is initialize, When resetStateVakue called, Then verify the state`() = runTest {
        // Given

        // When
        viewModel.resetStateValue()

        // Then
        TestCase.assertEquals(
            viewModel.state.value,
            TodoViewModel.UiState.Loading
        )
    }
}