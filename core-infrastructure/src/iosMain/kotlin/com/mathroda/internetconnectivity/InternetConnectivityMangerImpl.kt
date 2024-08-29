@file:OptIn(ExperimentalForeignApi::class, ExperimentalForeignApi::class,
    ExperimentalForeignApi::class
)

package com.mathroda.internetconnectivity

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.memScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import platform.SystemConfiguration.SCNetworkReachabilityCreateWithName
import platform.SystemConfiguration.SCNetworkReachabilityFlagsVar
import platform.SystemConfiguration.SCNetworkReachabilityGetFlags
import platform.SystemConfiguration.kSCNetworkFlagsConnectionRequired
import platform.SystemConfiguration.kSCNetworkFlagsReachable
import kotlinx.cinterop.*


class InternetConnectivityMangerImpl : InternetConnectivityManger {

    override fun getState(): Flow<InternetState> {
        return flow {
            val reachability = SCNetworkReachabilityCreateWithName(null, "www.apple.com")

            if (reachability == null) {
                emit(InternetState.NotAvailable)
                return@flow
            }

            memScoped {
                val flags = alloc<SCNetworkReachabilityFlagsVar>()
                val isReachable = SCNetworkReachabilityGetFlags(reachability, flags.ptr) && (flags.value.toInt() and kSCNetworkFlagsReachable.toInt() != 0) && (flags.value.toInt() and kSCNetworkFlagsConnectionRequired.toInt() == 0)

                if (isReachable) {
                    emit(InternetState.Available)
                } else {
                    emit(InternetState.NotAvailable)
                }
            }
        }
    }
}