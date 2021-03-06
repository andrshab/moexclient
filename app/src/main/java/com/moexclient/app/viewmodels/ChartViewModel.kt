package com.moexclient.app.viewmodels

import android.content.SharedPreferences
import android.graphics.Color
import androidx.lifecycle.*
import com.moexclient.app.game.Game
import com.moexclient.app.api.Exceptions
import com.moexclient.app.api.ErrorCallback
import com.moexclient.app.data.MoexRepository
import com.moexclient.app.data.Price
import com.moexclient.app.data.local.LocalRepository
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.math.round


class ChartViewModel @Inject constructor(private val repository: MoexRepository,
                                         private val sPrefs: SharedPreferences,
                                         private val localRepository: LocalRepository) : ViewModel() {
    val priceData: MutableLiveData<LineData> = MutableLiveData()
    val secName: MutableLiveData<String> = MutableLiveData()
    var prices: List<Price> = listOf()
    private var currentEntryIndex: Int = 0
    val chartEdge: MutableLiveData<Edges> = MutableLiveData()
    val isGameRunning: MutableLiveData<Boolean> = MutableLiveData()
    val sum: MutableLiveData<Float> = MutableLiveData()
    val startSum: MutableLiveData<Float> = MutableLiveData()
    val moneyLoc: MutableLiveData<String> = MutableLiveData()
    val buyBtn: MutableLiveData<Int> = MutableLiveData()
    val sellBtn: MutableLiveData<Int> = MutableLiveData()
    val toggleBtn: MutableLiveData<ToggleState> = MutableLiveData()
    val isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val profit: MutableLiveData<Float> = MutableLiveData()
    val isNewRecord: MutableLiveData<Boolean> = MutableLiveData()
    val adState: MutableLiveData<Int> = MutableLiveData()
    val chartState: MutableLiveData<Int> = MutableLiveData()
    lateinit var networkError: () -> Unit
    var isAdShowing: Boolean = false
    private var adCounter: Int = 0
    val game = Game()


    fun updateChart() {
        viewModelScope.launch(Exceptions.handler + ErrorCallback{networkError.invoke()}) {
            isLoading.value = true
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
                isLoading.value = false
                prices = secData.prices
                currentEntryIndex = prices.size/4
                game.reset(prices.subList(0, currentEntryIndex))
                startSum.value = game.startSum


                chartEdge.value = Edges(
                    prices.reduce(Price.Compare::minDate).date.time.toFloat(),
                    prices.reduce(Price.Compare::maxDate).date.time.toFloat(),
                    prices.reduce(Price.Compare::minVal).value,
                    prices.reduce(Price.Compare::maxVal).value
                )

                secName.value = randomSecsItem.name

                showNextPrice()
                isGameRunning.value = true
            } else {
                secName.value = "No data for ${randomSecsItem.name}"
                priceData.value = LineData()
            }

        }

    }
    fun buyAll() {
        buy((game.bank/game.stocksPrice).toInt() * 2)
    }
    fun sellAll() {
        sell(game.stocksNumber * 2)
    }

    fun buy(number: Int)  {
        game.buy(number)
        sum.value = (game.stocks + game.bank).roundToFirst()
    }

    fun sell(number: Int) {
        game.sell(number)
        sum.value = (game.stocks + game.bank).roundToFirst()
    }

    fun animate(isTrue: Boolean) {
        viewModelScope.launch(Dispatchers.Main) {
            val time = settingToTime(sPrefs.getString("time", "normal")?:"normal")
            val durationMillis = time / prices.size
            delay(durationMillis)
            if(isTrue && isGameRunning.value == true){
                showNextPrice()
            }
        }
    }

    private fun currentPrice(dataSet: LineDataSet): Float{
        val entries = dataSet.values
        return if(entries.isNotEmpty()) entries.last().y else 0f
    }

    fun showNextPrice(): Boolean {
        val dataSet = dataSet(prices.subList(0, currentEntryIndex), Color.BLUE, true, "primary")
        val lineData = LineData(dataSet)
        if(currentEntryIndex < prices.size) {
            game.stocksPrice = currentPrice(dataSet)//prices[currentEntryIndex].value
            game.stocks = game.stocksNumber * game.stocksPrice
            sum.value = (game.stocks + game.bank).roundToFirst()
            profit.value = (game.stocks + game.bank - game.startSum).roundToFirst()//(((game.stocks + game.bank)/game.startSum - 1)*100).roundToFirst()
            priceData.value = lineData
            currentEntryIndex += 1
            return true
        } else {
            isGameRunning.value = false
            saveIfRecord()
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

    private fun Float.roundToFirst(): Float {
        return round( this * 10.0f) / 10
    }
    private fun settingToTime(setting: String): Long =
        when(setting) {
            "slow" -> Game.CONSTANTS.SLOW
            "normal" -> Game.CONSTANTS.NORMAL
            "fast" -> Game.CONSTANTS.FAST
            else -> Game.CONSTANTS.NORMAL
        }
    private fun saveIfRecord() {
        viewModelScope.launch {
//            localRepository.checkProfit(profit.value)
            if(profit.value?:0f > 0) {
                isNewRecord.value = true
                isNewRecord.value = false
            }
            localRepository.saveRecord(secName.value, profit.value, startSum.value, sum.value)
        }
    }

    fun checkAdCounter(): Boolean {
        return adCounter == Constants.AD_COUNTER_MAX
    }

    fun incAdCounter() {
        adCounter = if(adCounter < Constants.AD_COUNTER_MAX) adCounter.plus(1) else 0

    }
    object Constants {
        const val AD_COUNTER_MAX = 5
    }

}

data class Edges(val xMin: Float, val xMax: Float, val yMin: Float, val yMax: Float)
data class ToggleState(val vis: Int, val isChecked: Boolean)