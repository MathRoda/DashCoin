package com.mathroda.core.util

import java.text.NumberFormat
import java.util.*
import java.util.regex.Pattern

fun numbersToCurrency(number: Int): String {
    val numberFormat = NumberFormat.getCurrencyInstance()
    numberFormat.currency = Currency.getInstance("USD")
    return numberFormat.format(number)
}

fun numbersToFormat(number: Int): String {
    val numberFormat = NumberFormat.getNumberInstance()
    return numberFormat.format(number)
}

fun isValidEmail(email: String): Boolean {
    return email.isNotEmpty() && Pattern.compile(Constants.EMAIL_REGEX).matcher(email).matches()
}

fun isValidPassword(password: String): Boolean {
    return password.isNotEmpty() && password.length >= 6
}