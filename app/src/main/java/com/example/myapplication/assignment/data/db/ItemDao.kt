package com.example.myapplication.assignment.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapplication.assignment.data.ItemEntity

@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(item: ItemEntity)

    @Query("SELECT * FROM items")
    suspend fun getAllItems(): List<ItemEntity>

}