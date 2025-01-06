package com.example.myapplication.assignment.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myapplication.assignment.utils.UtilCons

@Entity(tableName = UtilCons.TABLE_NAME)
data class ItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val item: String = "",
    )
