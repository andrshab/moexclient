package com.example.moexclient

import com.github.mikephil.charting.formatter.ValueFormatter
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

class DateValueFormatter: ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        val date = Date(value.toLong())
        val localDate: LocalDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        return localDate.month.name.take(3) + localDate.year
    }
}