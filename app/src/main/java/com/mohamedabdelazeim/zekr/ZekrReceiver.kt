package com.mohamedabdelazeim.zekr

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ZekrReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            if (ZekrPrefs.isEnabled(context)) {
                ZekrScheduler.schedule(context)
            }
        }
    }
}
