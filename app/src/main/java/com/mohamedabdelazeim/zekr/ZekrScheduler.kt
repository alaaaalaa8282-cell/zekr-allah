package com.mohamedabdelazeim.zekr

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

object ZekrScheduler {

    private const val WORK_NAME = "zekr_work"

    fun schedule(context: Context) {
        val minutes = ZekrPrefs.getIntervalMinutes(context).toLong()

          PeriodicWorkRequestBuilder<ZekrWorker>(maxOf(minutes, 15L), TimeUnit.MINUTES)
            .setInitialDelay(minutes, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            WORK_NAME,
            ExistingPeriodicWorkPolicy.REPLACE,
            request
        )
    }

    fun cancel(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
    }
}
