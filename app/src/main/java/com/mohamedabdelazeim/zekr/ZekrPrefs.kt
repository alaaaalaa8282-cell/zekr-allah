package com.mohamedabdelazeim.zekr

import android.content.Context
import android.content.SharedPreferences

object ZekrPrefs {
    private const val PREFS_NAME = "zekr_prefs"
    private const val KEY_INTERVAL_MINUTES = "interval_minutes"
    private const val KEY_ENABLED = "enabled"
    private const val KEY_CURRENT_INDEX = "current_index"

    private fun prefs(context: Context): SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun getIntervalMinutes(context: Context): Int =
        prefs(context).getInt(KEY_INTERVAL_MINUTES, 30)

    fun setIntervalMinutes(context: Context, minutes: Int) =
        prefs(context).edit().putInt(KEY_INTERVAL_MINUTES, minutes).apply()

    fun isEnabled(context: Context): Boolean =
        prefs(context).getBoolean(KEY_ENABLED, false)

    fun setEnabled(context: Context, enabled: Boolean) =
        prefs(context).edit().putBoolean(KEY_ENABLED, enabled).apply()

    fun getCurrentIndex(context: Context): Int =
        prefs(context).getInt(KEY_CURRENT_INDEX, 0)

    fun nextIndex(context: Context): Int {
        val current = getCurrentIndex(context)
        val next = (current + 1) % ZekrData.list.size
        prefs(context).edit().putInt(KEY_CURRENT_INDEX, next).apply()
        return current
    }
}
