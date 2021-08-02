package com.example.moexclient.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import retrofit2.http.GET

class News constructor(
    @SerializedName("content")
    val responseParts: ResponseParts
) {
    val id: String
        get() = responseParts.data[0][responseParts.col("id")]
    val title: String
        get() = responseParts.data[0][responseParts.col("title")]
    val publishedAt: String
        get() = responseParts.data[0][responseParts.col("published_at")]
    val text: String
        get() = responseParts.data[0][responseParts.col("body")]
}