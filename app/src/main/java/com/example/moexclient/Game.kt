package com.example.moexclient

import com.example.moexclient.data.Price
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
        bank = 10f.pow(ceil(stocksPrice).toInt().length())*100 //stocks
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
        const val durationMillis = 20000
    }
}