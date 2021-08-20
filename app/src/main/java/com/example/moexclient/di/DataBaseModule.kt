package com.example.moexclient.di

import android.content.Context
import androidx.room.Room
import com.example.moexclient.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataBaseModule(private val context: Context) {
    @Provides
    @Singleton
    fun provideDataBase(): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java, "app_database"
        ).build()
    }
}