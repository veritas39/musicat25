package com.example.musicat25

import android.location.Location
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.example.musicat25.R
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val songs = listOf(
        "https://buatlogin11.github.io/3sq/assets/override.mp3",
        "https://buatlogin11.github.io/3sq/assets/104hz.mp3",
        "https://buatlogin11.github.io/3sq/assets/kawaikutegomen.mp3",
        "https://buatlogin11.github.io/3sq/assets/VOID.mp3",
        "https://buatlogin11.github.io/3sq/assets/BleedingHearts.mp3",
        "https://buatlogin11.github.io/3sq/assets/Bug.mp3"
    )
    private val songTitles = listOf("Override", "104Hz", "Kawaikute Gomen", "VOID", "Bleeding Hearts", "Bug")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val musicListView = findViewById<ListView>(R.id.musicListView)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, songTitles)
        musicListView.adapter = adapter

        musicListView.setOnItemClickListener { _, _, position, _ ->
            val songUrl = songs[position]
            val songTitle = songTitles[position]
            playMusic(songUrl)
            logMusicPlay(songTitle)
        }

        val viewLogButton = findViewById<Button>(R.id.viewLogButton)
        viewLogButton.setOnClickListener {
            startActivity(Intent(this, LogActivity::class.java))
        }
    }

    private fun playMusic(songUrl: String) {
        if (::mediaPlayer.isInitialized && mediaPlayer.isPlaying) {
            mediaPlayer.stop()
            mediaPlayer.release()
        }
        mediaPlayer = MediaPlayer().apply {
            setDataSource(songUrl)
            prepare()
            start()
        }
    }

    private fun logMusicPlay(musicTitle: String) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 1)
        } else {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    val logEntry = "The song $musicTitle is played at ${location?.latitude}, ${location?.longitude}\n"
                    val logFile = File(filesDir, "music_log.txt")
                    FileOutputStream(logFile, true).bufferedWriter().use { writer ->
                        writer.write(logEntry)
                    }
                }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::mediaPlayer.isInitialized) {
            mediaPlayer.release()
        }
    }
}
