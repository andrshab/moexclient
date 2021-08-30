package com.moexclient.app.data

import com.google.gson.annotations.SerializedName

data class ResponseParts(
    @SerializedName("data")
    val data: List<List<String>>,
    @SerializedName("columns")
    val columns: List<String>
)