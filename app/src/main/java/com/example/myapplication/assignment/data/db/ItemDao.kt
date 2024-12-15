package com.example.myapplication.assignment.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapplication.assignment.data.ItemEntity

@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: ItemEntity)

    @Query("SELECT * FROM items")
    suspend fun getAllUsers(): List<ItemEntity>

    @Query("SELECT * FROM items ORDER BY id DESC LIMIT 1")
    suspend fun getLastInsertedRecord(): ItemEntity
}