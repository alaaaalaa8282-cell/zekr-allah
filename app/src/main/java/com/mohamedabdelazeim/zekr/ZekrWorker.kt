package com.mohamedabdelazeim.zekr

import android.content.Context
import android.content.Intent
import androidx.work.Worker
import androidx.work.WorkerParameters

class ZekrWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        val intent = Intent(applicationContext, ZekrService::class.java)
        applicationContext.startForegroundService(intent)
        return Result.success()
    }
}
