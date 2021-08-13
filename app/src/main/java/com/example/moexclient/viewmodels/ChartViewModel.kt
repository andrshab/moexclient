package com.example.moexclient.viewmodels

import android.graphics.Color
import android.util.Log
import androidx.lifecycle.*
import com.example.moexclient.api.Exceptions
import com.example.moexclient.data.MoexRepository
import com.example.moexclient.data.Price
import com.github.mikephil.charting.charts.CombinedChart
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
    val xAxisMax: MutableLiveData<Float> = MutableLiveData()
    val statistics: MutableLiveData<Int> = MutableLiveData()
    var totalClicks: Int = 0
    var trueClicks: Int = 0
    var trueLineColor: Int = 0
    var realPrices: List<Price> = listOf()
    fun updateChart() {
        viewModelScope.launch(Exceptions.handler) {
            val topSecsData = repository.getTopSecsData()
            val randomSecsItem = topSecsData.secIdList.random()
            val boardId = randomSecsItem.boardId
            val secId = randomSecsItem.secId
            var secData = repository.getSecOnBoardData(secId, boardId = boardId, sortOrder = "asc")
            Log.d("ChartViewModel", randomSecsItem.name.toString() + boardId)
            if (secData.prices.isNotEmpty()) {
                val startDate = secData.prices[0].date
                secData = repository.getSecOnBoardData(secId, boardId = boardId, sortOrder = "desc")
                val endDate = secData.prices[0].date
                val randDate = randDateString(startDate, endDate)
                secData = repository.getSecOnBoardData(secId, boardId = boardId, from = randDate)
                //split prices
                val (primary, real, fake) = splitPrices(secData.prices)
                realPrices = real

                var realColor: Int
                var fakeColor: Int
                if(real.last().value > fake.last().value) {
                    realColor = Color.GREEN
                    fakeColor = Color.RED
                } else {
                    realColor = Color.RED
                    fakeColor = Color.GREEN
                }
                trueLineColor = realColor

                val primaryDataSet = dataSet(primary, Color.BLUE, true, "primary")
                val realDataSet = dataSet(real, realColor, false, "real")
                val fakeDataSet = dataSet(fake, fakeColor, false,"fake")
                val lineData = LineData()
                lineData.addDataSet(primaryDataSet)
                lineData.addDataSet(realDataSet)
                lineData.addDataSet(fakeDataSet)

                val combinedData = CombinedData()
                combinedData.setData(lineData)

                xAxisMax.value = realDataSet.xMax
                chartData.value = combinedData
                secName.value = randomSecsItem.name
            } else {
                secName.value = "No data for ${randomSecsItem.name}"
                chartData.value?.setData(LineData())
            }

        }

    }

    fun showAnswer() {
        for(price in realPrices) {
            chartData.value?.lineData?.addEntry(Entry(price.date.time.toFloat(), price.value),0)
        }
    }

    fun isTrueColor(clickedColor: Int): Boolean {
        totalClicks += 1
        if(clickedColor == trueLineColor) {
            trueClicks += 1
        }
        statistics.value = (100*trueClicks)/totalClicks
        Log.d("isTrueColor", "true = $trueClicks, total = $totalClicks, stats = ${statistics.value}")
        return clickedColor == trueLineColor
    }

    private fun splitPrices(prices: List<Price>): Split {
        val primary = mutableListOf<Price>()
        val real = mutableListOf<Price>()

        val splitPoint = prices.size*2/3
        for (i in 0..splitPoint) {
            primary.add(prices[i])
        }
        for (i in splitPoint until prices.size) {
            real.add(prices[i])
        }
        val fake = fakePrices(real)

        return Split(primary, real, fake)
    }

    private fun fakePrices(real: List<Price>): List<Price> {
        val fake = mutableListOf<Price>()

        val fakeValues = fakeValues(real)
        val fakeDates = mutableListOf<Date>()
        for (price in real) {
            fakeDates.add(price.date)
        }

        for (i in real.indices) {
            fake.add(Price(fakeDates[i], fakeValues[i]))
        }
        return fake
    }


    private fun fakeValues(real: List<Price>): List<Float> {
//        val minFake = real.reduce(Price.Compare::min)
//        val maxFake = real.reduce(Price.Compare::max)
        val fakeValues = mutableListOf<Float>()
        val base = mutableListOf<Float>()
        val realValues = mutableListOf<Float>()
        for(price in real) {
            realValues.add(price.value)
        }

        val sigma = realValues.standardDeviation()
        val average = realValues.average()
        val baseRandNum = 1 + (100*sigma/average).toInt()
        Log.d("ChartViewModel", "sigma = $sigma, average = $average, randNum = $baseRandNum")

        var left = 0.0f
        var right = Random().nextFloat()*sigma.toFloat()
        var j=0
        for(i in real.indices) {
            base.add(left + j*(right-left)/baseRandNum)
            j += 1
            if(i%baseRandNum == 0 && i > 0){
                j = 0
                left = right
                right = Random().nextFloat()*sigma.toFloat()
            }
        }

        for (i in real.indices) {
            if(i == 0) {
                fakeValues.add(real[0].value)
            } else {
                fakeValues.add(real[0].value + base[i])
            }

        }
        return fakeValues
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

    data class Split(val primary: List<Price>, val real: List<Price>, val fake: List<Price>)

}