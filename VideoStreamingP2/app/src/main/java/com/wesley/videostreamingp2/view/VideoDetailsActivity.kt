package com.wesley.videostreamingp2.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.wesley.videostreamingp2.R
import com.wesley.videostreamingp2.viewmodel.VideoDetailsViewModel

class VideoDetailsActivity : AppCompatActivity() {

    lateinit var videoDetailsViewModel: VideoDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_details)
    }

    override fun onStart() {
        super.onStart()

        videoDetailsViewModel = ViewModelProviders.of(this).get(VideoDetailsViewModel::class.java)
    }
}
