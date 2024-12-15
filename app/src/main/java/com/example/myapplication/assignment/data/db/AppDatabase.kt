package com.example.myapplication.assignment.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myapplication.assignment.data.ItemEntity

@Database(entities = [ItemEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): ItemDao
}