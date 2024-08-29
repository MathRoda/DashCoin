package com.mathroda.core.util

import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSNumberFormatterCurrencyStyle
import platform.Foundation.NSNumberFormatterDecimalStyle
import platform.Foundation.NSUUID
import platform.Foundation.numberWithInt

actual fun numbersToCurrency(number: Int, currencyCode: String): String {
    val formatter = NSNumberFormatter().apply {
        numberStyle = NSNumberFormatterCurrencyStyle
        this.currencyCode = currencyCode
    }
    val nsNumber = NSNumber.numberWithInt(number)
    return formatter.stringFromNumber(nsNumber) ?: "$currencyCode $number"
}

actual fun numbersToFormat(number: Int): String {
    val formatter = NSNumberFormatter().apply {
        numberStyle = NSNumberFormatterDecimalStyle
    }
    val nsNumber = NSNumber.numberWithInt(number)
    return formatter.stringFromNumber(nsNumber) ?: number.toString()
}

actual fun generateUUID(): String {
    return NSUUID().UUIDString()
}