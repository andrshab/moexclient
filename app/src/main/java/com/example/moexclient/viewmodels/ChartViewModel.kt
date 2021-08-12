package com.example.moexclient.viewmodels

import android.graphics.Color
import android.util.Log
import androidx.lifecycle.*
import com.example.moexclient.data.MoexRepository
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import javax.inject.Inject

class ChartViewModel @Inject constructor(private val repository: MoexRepository): ViewModel() {
    val chartData: MutableLiveData<LineData> = MutableLiveData()
    val secName: MutableLiveData<String> = MutableLiveData()
    fun updateChart() {
        viewModelScope.launch {
            val list = repository.getTopSecsData()
            val randomSecsItem = list.secIdList.random()
            val boardId = randomSecsItem.boardId
            val secId = randomSecsItem.secId
            var secData = repository.getSecOnBoardData(secId, boardId = boardId, sortOrder = "asc")
            Log.d("ChartViewModel", randomSecsItem.name.toString() + boardId)
            if(secData.prices.isNotEmpty()) {
                val startDate = secData.prices[0].date
                secData = repository.getSecOnBoardData(secId, boardId = boardId, sortOrder = "desc")
                val endDate = secData.prices[0].date
                val randDate = randDateString(startDate, endDate)
                secData = repository.getSecOnBoardData(secId, boardId = boardId, from = randDate)

                val entries = mutableListOf<Entry>()
                for(price in secData.prices) {
                    entries.add(Entry(price.date.time.toFloat(), price.value ))
                }
                val dataSet = LineDataSet(entries, "")
                dataSet.setDrawFilled(true)
                dataSet.setDrawValues(false)
                dataSet.fillColor = Color.BLUE

                val lineData = LineData(dataSet)
                chartData.value = lineData
                secName.value = randomSecsItem.name
            } else {
                secName.value = "No data for ${randomSecsItem.name}"
                chartData.value = LineData()
            }

        }

    }

    private fun randDateString(startDate: Date, endDate: Date): String {
        val c = Calendar.getInstance()
        c.time = endDate
        c.add(Calendar.MONTH, -3)
        val correctEndDate = c.time
        val rand = (startDate.time..correctEndDate.time).random()
        return SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(Date(rand))
    }

}