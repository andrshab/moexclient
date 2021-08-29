package com.example.moexclient.api

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

class ErrorCallback(val callback: () -> Unit): AbstractCoroutineContextElement(ErrorCallback) {
    companion object Key : CoroutineContext.Key<ErrorCallback>
}

object Exceptions {
    val handler = CoroutineExceptionHandler { coroutineContext , exception ->
        println("CoroutineExceptionHandler got $exception")
        val callback: (() -> Unit)? = coroutineContext[ErrorCallback.Key]?.callback
        callback?.invoke()
    }
}