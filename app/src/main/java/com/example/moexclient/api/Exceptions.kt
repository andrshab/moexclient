package com.example.moexclient.api

import kotlinx.coroutines.CoroutineExceptionHandler

object Exceptions {
    val handler = CoroutineExceptionHandler { _, exception ->
        println("CoroutineExceptionHandler got $exception")
    }
}