package com.mathroda.core.util

import java.text.NumberFormat
import java.util.Currency
import java.util.UUID

actual fun numbersToCurrency(number: Int, currencyCode: String): String {
    val numberFormat = NumberFormat.getCurrencyInstance().apply {
        currency = Currency.getInstance(currencyCode)
    }
    return numberFormat.format(number)
}

actual fun numbersToFormat(number: Int): String {
    val numberFormat = NumberFormat.getNumberInstance()
    return numberFormat.format(number)
}

actual fun generateUUID(): String {
    return UUID.randomUUID().toString()
}