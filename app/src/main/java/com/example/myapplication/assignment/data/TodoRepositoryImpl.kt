package com.example.myapplication.assignment.data

import com.example.myapplication.assignment.data.db.ItemDao
import com.example.myapplication.assignment.domain.TodoItem
import com.example.myapplication.assignment.domain.TodoRepository
import com.example.myapplication.assignment.domain.toDomain
import com.example.myapplication.assignment.domain.toEntity
import javax.inject.Inject

class TodoRepositoryImpl @Inject constructor(
    private val userDao: ItemDao
) : TodoRepository {
    override suspend fun saveItem(item: TodoItem) {
        userDao.insertUser(item.toEntity())
    }

    override suspend fun getAllItems(): List<TodoItem> {
        return userDao.getAllItems().map { userEntities ->
            userEntities.toDomain()
        }
    }
}