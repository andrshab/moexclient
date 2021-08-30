package com.moexclient.app.data

import android.util.Log
import com.moexclient.app.api.ApiConstants
import java.text.SimpleDateFormat
import java.util.*

class SecData(private val historyList: HistoryList) {
    val prices: List<Price>
        get() {
            val tmpList = mutableListOf<Price>()
            val keys = historyList.responseParts.columns
            for (item in historyList.responseParts.data) {
                val lm = keys.zip(item).toMap()
                try {
                    val date = SimpleDateFormat(
                        "yyyy-MM-dd",
                        Locale.ENGLISH
                    ).parse(lm[ApiConstants.TRADE_DATE] ?: "null")
                    val value = lm[ApiConstants.PRICE_WA]?.toFloat()
                    if(date!= null && value != null) {
                        tmpList.add(Price(date, value))
                    }

                } catch (e: Exception) {
                    Log.d("SecData", "Unable to make date or price value. " + e.message)
                }
            }
            return tmpList
        }
}

data class Price(val date: Date, val value: Float) {
    internal object Compare {
        fun minVal(a: Price, b: Price): Price {
            return if (a.value < b.value) a else b
        }
        fun maxVal(a: Price, b: Price): Price {
            return if(a.value > b.value) a else b
        }
        fun minDate(a: Price, b: Price): Price {
            return if (a.date < b.date) a else b
        }
        fun maxDate(a: Price, b: Price): Price {
            return if(a.date > b.date) a else b
        }
    }
}