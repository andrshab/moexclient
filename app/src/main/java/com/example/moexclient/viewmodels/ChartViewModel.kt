package com.example.moexclient.viewmodels

import android.graphics.Color
import androidx.lifecycle.*
import com.example.moexclient.api.Exceptions
import com.example.moexclient.data.MoexRepository
import com.example.moexclient.data.Price
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class ChartViewModel @Inject constructor(private val repository: MoexRepository) : ViewModel() {
    val priceData: MutableLiveData<LineData> = MutableLiveData()
    val secName: MutableLiveData<String> = MutableLiveData()
    var prices: List<Price> = listOf()
    var currentEntryIndex: Int = 0
    val chartEdge: MutableLiveData<Edges> = MutableLiveData()
    val isFinished: MutableLiveData<Boolean> = MutableLiveData()

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
                prices = secData.prices

                chartEdge.value = Edges(
                    prices.reduce(Price.Compare::minDate).date.time.toFloat(),
                    prices.reduce(Price.Compare::maxDate).date.time.toFloat(),
                    prices.reduce(Price.Compare::minVal).value,
                    prices.reduce(Price.Compare::maxVal).value
                )

                secName.value = randomSecsItem.name

                showNextPrice()
            } else {
                secName.value = "No data for ${randomSecsItem.name}"
                priceData.value = LineData()
            }

        }

    }

    fun animate(isTrue: Boolean) {
        viewModelScope.launch(Dispatchers.Main) {
            delay(100)
            if(isTrue){
                showNextPrice()
            }
        }
    }

    fun currentPrice(dataSet: LineDataSet): Float{
        val entries = dataSet.values
        return if(entries.isNotEmpty()) entries.last().y else 0f
    }

    fun showNextPrice(): Boolean {
        val dataSet = dataSet(prices.subList(0, currentEntryIndex), Color.BLUE, true, "primary")
        val lineData = LineData(dataSet)
        if(currentEntryIndex < prices.size) {
            priceData.value = lineData
            currentEntryIndex += 1
            return true
        } else {
            isFinished.value = true
            currentEntryIndex = 0
            return false
        }
    }

    private fun dataSet(prices: List<Price>, color: Int, filled: Boolean, label: String): LineDataSet {
        val entries = mutableListOf<Entry>()
        for (price in prices) {
            entries.add(Entry(price.date.time.toFloat(), price.value))
        }
        val dataSet = LineDataSet(entries, label)
        dataSet.setDrawFilled(filled)
        dataSet.setDrawValues(true)
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

data class Edges(val xMin: Float, val xMax: Float, val yMin: Float, val yMax: Float)