package com.mathroda.core.util

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn

expect fun numbersToCurrency(number: Int, currencyCode:String = "USD"): String

expect fun numbersToFormat(number: Int): String

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
