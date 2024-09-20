package com.mathroda.core.util

import com.mathroda.core.BuildKonfig
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn

fun numbersToCurrency(number: Int, currencyCode: String = "USD"): String {
    val formattedNumber = numbersToFormat(number)
    return "$currencyCode $formattedNumber"
}


fun numbersToFormat(number: Int): String {
    val numString = number.toString()
    val result = StringBuilder()

    var counter = 0
    for (i in numString.length - 1 downTo 0) {
        result.insert(0, numString[i])
        counter++
        if (counter % 3 == 0 && i != 0) {
            result.insert(0, ',')
        }
    }

    return result.toString()
}



expect fun generateUUID(): String

fun isValidEmail(email: String): Boolean {
    val emailRegex = Regex(Constants.EMAIL_REGEX)
    return email.isNotEmpty() && emailRegex.matches(email)
}

fun isValidPassword(password: String): Boolean {
    return password.isNotEmpty() && password.length >= 6
}

fun getCurrentDate(): LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault())

fun getCurrentDateTime(): LocalDateTime {
    return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
}

fun currentTimeMillis() = Clock.System.now().toEpochMilliseconds()

class AppInfo {
    fun getVersion() = BuildKonfig.VERSION_NAME
}