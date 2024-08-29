@file:OptIn(ExperimentalForeignApi::class, ExperimentalForeignApi::class)

package com.mathroda.phoneshaking

import dev.gitlive.firebase.firestore.toException
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.copy
import kotlinx.cinterop.useContents
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import platform.CoreMotion.CMAccelerometerData
import platform.CoreMotion.CMMotionManager
import platform.Foundation.NSError
import platform.Foundation.NSOperationQueue
import platform.darwin.dispatch_get_main_queue
import kotlin.math.sqrt

class PhoneShakingManagerImpl : PhoneShakingManger {
    private val motionManager = CMMotionManager()

    init {
        getState() // Start monitoring
    }

    override fun getState(): Flow<PhoneShakingState> {
        return callbackFlow {
            trySend(PhoneShakingState.IDLE)

            // Check if accelerometer is available
            if (motionManager.accelerometerAvailable) {
                motionManager.accelerometerUpdateInterval = 0.1 // Set update interval to 100 ms

                // Define the accelerometer handler
                val handler: (CMAccelerometerData?, NSError?) -> Unit = { data, error ->
                    data?.let {
                        // Use useContents to access x, y, z properties
                        it.acceleration.useContents {
                            val x = x
                            val y = y
                            val z = z

                            lastAcceleration = currentAcceleration

                            // Getting current acceleration with the help of fetched x, y, z values
                            currentAcceleration = sqrt((x * x + y * y + z * z)).toFloat()
                            val delta: Float = currentAcceleration - lastAcceleration
                            this@PhoneShakingManagerImpl.acceleration = this@PhoneShakingManagerImpl.acceleration * 0.9f + delta

                            // Check if acceleration value is over threshold
                            if (this@PhoneShakingManagerImpl.acceleration > 16) {
                                trySend(PhoneShakingState.IsShaking)
                            }
                        }
                    }
                }

                // Start accelerometer updates
                motionManager.startAccelerometerUpdatesToQueue(
                    queue = NSOperationQueue.mainQueue,
                    withHandler = handler
                )
            }

            awaitClose { motionManager.stopAccelerometerUpdates() }
        }
    }

    // Variables to store acceleration data
    private var acceleration = 0f
    private var currentAcceleration = 0f
    private var lastAcceleration = 0f
}