package com.mathroda.phoneshaking

import android.app.Application
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.Objects
import javax.inject.Inject
import kotlin.math.sqrt

class PhoneShakingMangerImpl @Inject constructor(
    @ApplicationContext private val context: Application
): PhoneShakingManger {

    private val sensorManger = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var acceleration = 0f
    private var currentAcceleration = 0f
    private var lastAcceleration = 0f

    init {
        getState()
    }

    override fun getState(): Flow<PhoneShakingState> {
        return callbackFlow {
            trySend(PhoneShakingState.IDLE)

            val callback: SensorEventListener = object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent?) {
                    event?.let { sensorEvent ->
                        val x = sensorEvent.values[0]
                        val y = sensorEvent.values[1]
                        val z = sensorEvent.values[2]
                        lastAcceleration = currentAcceleration

                        // Getting current accelerations
                        // with the help of fetched x,y,z values
                        currentAcceleration = sqrt((x * x + y * y + z * z).toDouble()).toFloat()
                        val delta: Float = currentAcceleration - lastAcceleration
                        acceleration = acceleration * 0.9f + delta


                        // acceleration value is over 16
                        if (acceleration > 16) {
                            trySend(PhoneShakingState.IsShaking)
                        }
                    }
                }

                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
            }

            Objects.requireNonNull(sensorManger)
                .registerListener(
                    callback,
                    sensorManger.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                    SensorManager.SENSOR_DELAY_NORMAL
                )

            awaitClose { sensorManger.unregisterListener(callback) }
        }
    }
}