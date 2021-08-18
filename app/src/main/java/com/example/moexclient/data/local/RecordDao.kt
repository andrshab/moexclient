package com.example.moexclient.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RecordDao {
    @Query("SELECT * FROM records ORDER BY profit DESC")
    suspend fun getAll(): List<Record>
    @Query("SELECT * FROM records WHERE profit > :p")
    suspend fun geAllGreaterThan(p: Float?): List<Record>
//    @Query("SELECT * FROM records WHERE id = :id")
//    suspend fun getRecordById(id: Int)
    @Insert
    suspend fun insert(record: Record)
}