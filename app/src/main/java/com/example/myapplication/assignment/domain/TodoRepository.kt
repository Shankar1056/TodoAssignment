package com.example.myapplication.assignment.domain

import com.example.myapplication.assignment.data.ItemEntity

interface TodoRepository {
    suspend fun saveUser(user: TodoItem)
    suspend fun getLastInsertedItem(): TodoItem
    suspend fun getAllUsers(): List<TodoItem>
}

fun ItemEntity.toDomain(): TodoItem {
    return TodoItem(item = this.item)
}

fun TodoItem.toEntity(): ItemEntity {
    return ItemEntity(item = this.item)
}
