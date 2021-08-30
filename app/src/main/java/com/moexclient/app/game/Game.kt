package com.moexclient.app.game

import com.moexclient.app.data.Price
import kotlin.math.*

class Game {

    var bank: Float = 0f
    var startBank: Float = 0f
    var stocksNumber: Int = 0
    var stocksPrice: Float = 0f
    var stocks: Float = 0f
    var startStocks: Float = 0f
    var startSum: Float = 0f

    fun reset(prices: List<Price>) {
        stocksPrice = prices.last().value
        stocksNumber = 0//100
        stocks = 0f//stocksPrice * stocksNumber
        startStocks = stocks
        bank = max(10f.pow(ceil(stocksPrice).toInt().length())*10, 100000f) //stocks
        startBank = bank
        startSum = startBank + startStocks
    }
    fun buy(number: Int) {
        if(bank - stocksPrice * number > 0) {
            stocksNumber += number
            stocks = stocksPrice * stocksNumber
            bank -= stocksPrice * number
        } else {
            stocksNumber += (bank/stocksPrice).toInt()
            stocks = stocksPrice * stocksNumber
            bank -= stocksPrice * (bank/stocksPrice).toInt()
        }
    }
    fun sell(number: Int) {
        if(stocksNumber - number >= 0) {
            stocksNumber -= number
            stocks = stocksPrice * stocksNumber
            bank += stocksPrice * number
        } else {
            bank += stocksPrice * stocksNumber
            stocksNumber -= stocksNumber
            stocks = stocksPrice * stocksNumber
        }
    }
    fun Int.length() = when(this) {
        0 -> 1
        else -> log10(abs(toDouble())).toInt() + 1
    }
    object CONSTANTS {
        const val FAST: Long = 5000
        const val NORMAL: Long = 30000
        const val SLOW: Long = 60000
    }
}