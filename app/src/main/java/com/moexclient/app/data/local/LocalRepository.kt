package com.moexclient.app.data.local

import javax.inject.Inject

class LocalRepository @Inject constructor(private val db: AppDatabase) {
    suspend fun saveRecord(name: String?, profit: Float?, startSum: Float?, sum: Float?) {
        db.userDao().insert(Record(name, profit, startSum, sum))
    }
    suspend fun getAll(): List<Record> {
        return db.userDao().getAll()
    }
    suspend fun checkProfit(profit: Float?): Boolean {
        return db.userDao().geAllGreaterThan(profit).isEmpty()
    }
    suspend fun clear() {
        db.userDao().deleteAll()
    }
}