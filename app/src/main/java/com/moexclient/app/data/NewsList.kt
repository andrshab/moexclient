package com.moexclient.app.data

import com.moexclient.app.api.ApiConstants
import com.google.gson.annotations.SerializedName

data class NewsList(
    @SerializedName("sitenews")
    val responseParts: ResponseParts,
    @SerializedName("sitenews.cursor")
    val cursorParts: ResponseParts
) {
    val list: List<NewsItem>
        get() {
            val list = mutableListOf<NewsItem>()
            for (item in responseParts.data) {
                val lm = responseParts.columns.zip(item).toMap()
                list.add(NewsItem(
                    lm[ApiConstants.ID]?.toInt(),
                    lm[ApiConstants.TITLE],
                    lm[ApiConstants.PUBLISHED_AT]
                ))
            }
            return list
        }
}

data class NewsItem(val id: Int? = 0, val title: String? = "null", val time: String? = "null")

