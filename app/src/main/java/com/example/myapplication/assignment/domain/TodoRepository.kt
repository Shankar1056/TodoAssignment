package com.example.myapplication.assignment.domain

import com.example.myapplication.assignment.data.ItemEntity

interface TodoRepository {
    suspend fun saveItem(user: TodoItem)
    suspend fun getAllItems(): List<TodoItem>
}

fun ItemEntity.toDomain(): TodoItem {
    return TodoItem(item = this.item)
}

fun TodoItem.toEntity(): ItemEntity {
    return ItemEntity(item = this.item)
}
