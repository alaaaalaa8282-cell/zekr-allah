package com.mohamedabdelazeim.zekr.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.mohamedabdelazeim.zekr.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestNotificationPermission()
        setupUI()
    }

    private fun setupUI() {
        val switchEnable = findViewById<Switch>(R.id.switchEnable)
        val spinnerInterval = findViewById<Spinner>(R.id.spinnerInterval)
        val tvStatus = findViewById<TextView>(R.id.tvStatus)

        // Interval options
        val intervals = listOf(15, 30, 60, 120)
        val intervalLabels = intervals.map { "كل $it دقيقة" }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, intervalLabels)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerInterval.adapter = adapter

        // Set saved values
        val savedInterval = ZekrPrefs.getIntervalMinutes(this)
        val savedIndex = intervals.indexOf(savedInterval).takeIf { it >= 0 } ?: 1
        spinnerInterval.setSelection(savedIndex)
        switchEnable.isChecked = ZekrPrefs.isEnabled(this)
        updateStatus(tvStatus, switchEnable.isChecked, savedInterval)

        // Interval change
        spinnerInterval.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View?, pos: Int, id: Long) {
                val selected = intervals[pos]
                ZekrPrefs.setIntervalMinutes(this@MainActivity, selected)
                if (switchEnable.isChecked) {
                    ZekrScheduler.schedule(this@MainActivity)
                }
                updateStatus(tvStatus, switchEnable.isChecked, selected)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Enable/disable
        switchEnable.setOnCheckedChangeListener { _, checked ->
            ZekrPrefs.setEnabled(this, checked)
            if (checked) {
                ZekrScheduler.schedule(this)
                Toast.makeText(this, "تم تفعيل الأذكار ✓", Toast.LENGTH_SHORT).show()
            } else {
                ZekrScheduler.cancel(this)
                Toast.makeText(this, "تم إيقاف الأذكار", Toast.LENGTH_SHORT).show()
            }
            updateStatus(tvStatus, checked, ZekrPrefs.getIntervalMinutes(this))
        }
    }

    private fun updateStatus(tv: TextView, enabled: Boolean, interval: Int) {
        tv.text = if (enabled) "✅ الأذكار مفعّلة — كل $interval دقيقة"
        else "⏸ الأذكار متوقفة"
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    100
                )
            }
        }
    }
}
