package com.example.moexclient.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import retrofit2.http.GET

data class News(
    @SerializedName("content")
    val responseParts: ResponseParts
) {
    val map: Map<String, String>?
        get() {
            return if(responseParts.data.isNotEmpty()) responseParts.columns.zip(responseParts.data[0]).toMap() else null
        }
}