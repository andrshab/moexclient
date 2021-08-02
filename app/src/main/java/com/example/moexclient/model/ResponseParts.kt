package com.example.moexclient.model

import com.google.gson.annotations.SerializedName

class ResponseParts(
    @SerializedName("data")
    val data: List<List<String>>,
    @SerializedName("columns")
    val columns: List<String>
) {
    fun col(key: String): Int = columns.indexOf(key)
}