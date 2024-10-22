package com.manipur.khannasi.util

import kotlinx.coroutines.delay

class RetryHelper(
    private val maxRetries: Int,
    private val initialDelay: Long,
    private val maxDelay: Long,
    private val factor: Double
) {
    suspend fun <T> retry(block: suspend () -> T?): T? {
        var currentDelay = initialDelay
        repeat(maxRetries) {
            val result = block()
            if (result != null) return result
            delay(currentDelay)
            currentDelay = (currentDelay * factor).coerceAtMost(maxDelay.toDouble()).toLong()
        }
        return block() // Last attempt
    }
}