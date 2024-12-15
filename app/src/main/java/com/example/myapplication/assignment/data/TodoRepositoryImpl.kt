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
    override suspend fun saveUser(item: TodoItem) {
        userDao.insertUser(item.toEntity())
    }

    override suspend fun getLastInsertedItem(): TodoItem {
        return userDao.getLastInsertedRecord().toDomain()
    }

    override suspend fun getAllUsers(): List<TodoItem> {
        return userDao.getAllUsers().map { userEntities ->
            userEntities.toDomain()
        }
    }
}