package com.example.moexclient.model

import com.google.gson.annotations.SerializedName

data class ResponseParts(
    @SerializedName("data")
    val data: List<List<String>>,
    @SerializedName("columns")
    val columns: List<String>
)