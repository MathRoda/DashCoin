package com.mathroda.common.toastmessage.components

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TimerManager {
    private var timerJob: Job? = null

    fun scheduleTimer(
        scope: CoroutineScope,
        visibilityDuration: Long,
        onTimerTriggered: () -> Unit
    ) {
        timerJob?.cancel()
        timerJob = scope.launch {
            delay(visibilityDuration)
            onTimerTriggered()
        }
    }

    fun cancelTimer() {
        timerJob?.cancel()
        timerJob = null
    }
}