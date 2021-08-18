package com.example.moexclient.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Record::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): RecordDao
}