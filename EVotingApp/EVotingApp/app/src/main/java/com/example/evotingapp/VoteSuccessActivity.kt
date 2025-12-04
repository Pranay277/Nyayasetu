package com.example.evotingapp

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class VoteSuccessActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 🔥 Force Light Theme
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        setContentView(R.layout.activity_vote_success)

        val videoView = findViewById<VideoView>(R.id.successVideo)
        val videoUri = Uri.parse("android.resource://" + packageName + "/" + R.raw.vote_success)

        videoView.setVideoURI(videoUri)
        videoView.setOnCompletionListener { videoView.start() }
        videoView.start()

        val btnHome: Button = findViewById(R.id.btnHome)
        btnHome.setOnClickListener {
            finishAndRemoveTask()
        }
    }
}
