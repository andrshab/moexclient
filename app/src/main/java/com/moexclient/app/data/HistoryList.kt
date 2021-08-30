package com.moexclient.app.data

import com.google.gson.annotations.SerializedName

data class HistoryList(
    @SerializedName("history")
    val responseParts: ResponseParts,
    @SerializedName("history.cursor")
    val cursorParts: ResponseParts)

