package com.example.myapplication.assignment.di

import android.content.Context
import androidx.room.Room
import com.example.myapplication.assignment.data.TodoRepositoryImpl
import com.example.myapplication.assignment.data.db.AppDatabase
import com.example.myapplication.assignment.data.db.ItemDao
import com.example.myapplication.assignment.domain.TodoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "item_database"
        ).build()
    }

    @Provides
    fun provideUserDao(database: AppDatabase): ItemDao {
        return database.userDao()
    }

    @Provides
    fun provideUserRepository(userDeo: ItemDao): TodoRepository {
        return TodoRepositoryImpl(userDeo)
    }

}