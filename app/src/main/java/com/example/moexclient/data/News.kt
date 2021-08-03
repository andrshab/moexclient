package com.example.moexclient.data

import com.google.gson.annotations.SerializedName

data class News(
    @SerializedName("content")
    val responseParts: ResponseParts
) {
    val map: Map<String, String>
        get() {
            return responseParts.columns.zip(responseParts.data[0]).toMap()
        }
}