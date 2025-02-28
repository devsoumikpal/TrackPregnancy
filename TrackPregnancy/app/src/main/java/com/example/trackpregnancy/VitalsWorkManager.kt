package com.example.trackpregnancy

import android.content.Context
import androidx.work.*
import com.example.trackpregnancy.workers.VitalsReminderWorker
import java.util.concurrent.TimeUnit

object VitalsWorkManager {

    fun scheduleVitalsReminder(context: Context) {
        val workRequest = PeriodicWorkRequestBuilder<VitalsReminderWorker>(
            5, TimeUnit.HOURS// Executes every 5 hours
        ).setConstraints(
            Constraints.Builder()
                .setRequiresBatteryNotLow(true) // Only run when battery is not low
                .build()
        ).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "VitalsReminderWork",
            ExistingPeriodicWorkPolicy.KEEP, // Keeps the existing work
            workRequest
        )
    }
}
