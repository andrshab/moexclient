package com.example.moexclient.viewmodels

import android.graphics.Color
import android.util.Log
import androidx.lifecycle.*
import com.example.moexclient.api.Exceptions
import com.example.moexclient.data.MoexRepository
import com.example.moexclient.data.Price
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.data.CombinedData
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.coroutines.launch
import org.apache.commons.math3.geometry.euclidean.twod.Line
import org.nield.kotlinstatistics.standardDeviation
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class ChartViewModel @Inject constructor(private val repository: MoexRepository) : ViewModel() {
    val chartData: MutableLiveData<CombinedData> = MutableLiveData()
    val secName: MutableLiveData<String> = MutableLiveData()

    fun updateChart() {
        viewModelScope.launch(Exceptions.handler) {
            val topSecsData = repository.getTopSecsData()
            val randomSecsItem = topSecsData.secIdList.random()
            val boardId = randomSecsItem.boardId
            val secId = randomSecsItem.secId
            var secData = repository.getSecOnBoardData(secId, boardId = boardId, sortOrder = "asc")
            if (secData.prices.isNotEmpty()) {
                val startDate = secData.prices[0].date
                secData = repository.getSecOnBoardData(secId, boardId = boardId, sortOrder = "desc")
                val endDate = secData.prices[0].date
                val randDate = randDateString(startDate, endDate)
                secData = repository.getSecOnBoardData(secId, boardId = boardId, from = randDate)

                val dataSet = dataSet(secData.prices, Color.BLUE, true, "primary")
                val lineData = LineData()
                lineData.addDataSet(dataSet)
                val combinedData = CombinedData()
                combinedData.setData(lineData)

                chartData.value = combinedData
                secName.value = randomSecsItem.name
            } else {
                secName.value = "No data for ${randomSecsItem.name}"
                chartData.value?.setData(LineData())
            }

        }

    }

    private fun dataSet(prices: List<Price>, color: Int, filled: Boolean, label: String): LineDataSet {
        val entries = mutableListOf<Entry>()
        for (price in prices) {
            entries.add(Entry(price.date.time.toFloat(), price.value))
        }
        val dataSet = LineDataSet(entries, label)
        dataSet.setDrawFilled(filled)
        dataSet.setDrawValues(false)
        dataSet.fillColor = Color.BLUE
        dataSet.color = color
        dataSet.setDrawCircles(false)
        return dataSet
    }

    private fun randDateString(startDate: Date, endDate: Date): String {
        val c = Calendar.getInstance()
        c.time = endDate
        c.add(Calendar.MONTH, -3)
        val correctEndDate = c.time
        val rand: Long = if(correctEndDate.time > startDate.time) {
            (startDate.time..correctEndDate.time).random()
        } else {
            (startDate.time..endDate.time).random()
        }

        return SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(Date(rand))
    }

}