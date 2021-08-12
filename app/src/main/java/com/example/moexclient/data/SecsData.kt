package com.example.moexclient.data

import com.example.moexclient.api.ApiConstants

data class SecsData(private val historyList: HistoryList) {
    val secIdList: List<SecsListItem>
        get() {
            val tmpList = mutableListOf<SecsListItem>()
            val keys = historyList.responseParts.columns
            for (item in historyList.responseParts.data) {
                val lm = keys.zip(item).toMap()
                tmpList.add(SecsListItem(
                    lm[ApiConstants.SEC_ID],
                    lm[ApiConstants.SHORT_NAME],
                    lm[ApiConstants.BOARD_ID])
                )
            }
            return tmpList
        }
}

data class SecsListItem(val secId: String? = "null", val name: String? = "null", val boardId: String?)