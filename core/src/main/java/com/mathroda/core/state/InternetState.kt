package com.mathroda.core.state

sealed class InternetState {
    object Available: InternetState()
    object NotAvailable: InternetState()
    object IDLE: InternetState()
}