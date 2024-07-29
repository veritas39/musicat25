package com.example.musicat25

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.musicat25.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.io.File
import java.io.FileInputStream

class LogActivity : AppCompatActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var logTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        logTextView = findViewById(R.id.logTextView)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 1)
        } else {
            displayLog()
        }
    }

    private fun displayLog() {
        val logFile = File(filesDir, "music_log.txt")
        if (logFile.exists()) {
            val logText = FileInputStream(logFile).bufferedReader().use { it.readText() }
            logTextView.text = logText
        } else {
            logTextView.text = "No log data available."
        }
    }
}
