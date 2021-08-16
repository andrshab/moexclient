package com.example.moexclient

import com.example.moexclient.data.Price

class Game {

    var bank: Float = 0f
    var startBank: Float = 0f
    var stocksNumber: Int = 0
    var stocksPrice: Float = 0f
    var stocks: Float = 0f
    var startStocks: Float = 0f
    var startSum: Float = 0f

    fun reset(prices: List<Price>) {
        val averagePrice = prices.map { it.value }.average()
        stocksPrice = prices.last().value
        stocksNumber = 100
        stocks = stocksPrice * stocksNumber
        startStocks = stocks
        bank = (stocksNumber * averagePrice).toFloat()
        startBank = bank
        startSum = startBank + startStocks
    }
    fun buy(number: Int) {
        if(bank - stocksPrice * number > 0) {
            stocksNumber += number
            stocks = stocksPrice * stocksNumber
            bank -= stocksPrice * number
        }
    }
    fun sell(number: Int) {
        if(stocksNumber - number >= 0) {
            stocksNumber -= number
            stocks = stocksPrice * stocksNumber
            bank += stocksPrice * number
        }
    }
    object CONSTANTS {
        const val durationMillis = 60000
    }
}