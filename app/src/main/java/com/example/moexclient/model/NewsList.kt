package com.example.moexclient.model

import com.google.gson.annotations.SerializedName

data class NewsList(
    @SerializedName("sitenews")
    val responseParts: ResponseParts
) {
    val listMap: List<Map<String, String>>
        get() {
            val lm = mutableListOf<Map<String, String>>()
            for (item in responseParts.data) {
                lm.add(responseParts.columns.zip(item).toMap())
            }
            return lm
        }
}

