package com.example.moexclient.model

import com.google.gson.annotations.SerializedName

data class NewsList(
    @SerializedName("sitenews")
    val responseParts: ResponseParts
)

