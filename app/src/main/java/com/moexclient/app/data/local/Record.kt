package com.moexclient.app.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "records")
data class Record (
    @ColumnInfo(name = "sec_name") var sec_name: String?,
    @ColumnInfo(name = "profit") var profit: Float?,
    @ColumnInfo(name = "start_sum") var start_sum: Float?,
    @ColumnInfo(name = "sum") var sum: Float?,
    @PrimaryKey(autoGenerate = true) var id: Int = 0) {
}