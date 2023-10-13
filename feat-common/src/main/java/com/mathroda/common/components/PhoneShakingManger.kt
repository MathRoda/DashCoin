package com.mathroda.common.components

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import java.util.Objects
import kotlin.math.sqrt


/**
 * Unused but could a learning resource
 */
@Composable
internal fun PhoneShakingManger(
    onPhoneShaking: () -> Unit
) {
    val context = LocalContext.current
    val sensorManger = remember {
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }
    var acceleration by remember { mutableStateOf(0f) }
    var currentAcceleration by remember { mutableStateOf(0f) }
    var lastAcceleration by remember { mutableStateOf(0f) }

    DisposableEffect(sensorManger) {
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


                    // acceleration value is over 12
                    if (acceleration > 12) {
                        onPhoneShaking()
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

        onDispose { sensorManger.unregisterListener(callback) }
    }
}