package com.wesley.videostreamingp2.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.MediaController
import android.widget.TextView
import android.widget.VideoView
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.wesley.videostreamingp2.App
import com.wesley.videostreamingp2.R
import com.wesley.videostreamingp2.model.Video
import com.wesley.videostreamingp2.model.VideoDAO
import com.wesley.videostreamingp2.model.VideoService
import com.wesley.videostreamingp2.viewmodel.VideoDetailsViewModel

class VideoDetailsActivity : AppCompatActivity() {

    lateinit var videoDetailsViewModel: VideoDetailsViewModel
    lateinit var btnLike : Button
    lateinit var btnDislike : Button
    lateinit var videoView : VideoView
    lateinit var txtSource : TextView
    lateinit var txtSpeaker : TextView
    lateinit var txtLikes : TextView
    lateinit var txtTitle : TextView
    lateinit var txtDuration : TextView
    lateinit var txtDislikes: TextView
    lateinit var txtViews : TextView
    lateinit var txtRecorded : TextView
    lateinit var txtEvent : TextView

    var clickedId : Int = 0

    val attVideoInformation : BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.takeIf {
                it.hasExtra(videoDetailsViewModel.getVideoServiceContract().VIDEO_INFORMATION_EXTRA)
            }.apply {
                val video = intent?.getSerializableExtra(videoDetailsViewModel.getVideoServiceContract().VIDEO_INFORMATION_EXTRA) as Video

                txtSource.text   = video.source
                txtSpeaker.text  = video.speaker
                txtLikes.text    = video.likes.toString()
                txtTitle.text    = video.title
                txtDuration.text = video.duration
                txtDislikes.text = video.dislikes.toString()
                txtViews.text    = video.views.toString()
                txtRecorded.text = video.recorded
                txtEvent.text    = video.event
            }
        }

    }

    private val errorListReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.takeIf { it.hasExtra(videoDetailsViewModel.getVideoServiceContract().NOTIFY_ERROR_EXTRA) }.apply {
                intent?.getStringExtra(videoDetailsViewModel.getVideoServiceContract().NOTIFY_ERROR_EXTRA)?.let {
                    notify(it)
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_details)

    }

    override fun onStart() {
        super.onStart()

        videoDetailsViewModel = ViewModelProviders.of(this).get(VideoDetailsViewModel::class.java)
        clickedId = intent.getIntExtra("posicao", -1)

        val video = videoDetailsViewModel.getVideoAt(clickedId)

        bindComponents()
        setStreaming(video)
        setClickListeners(video)
        incrementView(video)
        attTextViews()

        App.registerBroadcast(attVideoInformation, IntentFilter(videoDetailsViewModel.getVideoServiceContract().VIDEO_INFORMATION))
        App.registerBroadcast(errorListReceiver, IntentFilter(videoDetailsViewModel.getVideoServiceContract().NOTIFY_ERROR))
    }

    private fun incrementView(video: Video) {
        Intent(this, VideoService::class.java).apply {
            action = videoDetailsViewModel.getVideoServiceContract().VIEW_INCREMENT_ACTION
            putExtra(videoDetailsViewModel.getVideoServiceContract().VIEW_INCREMENT_EXTRA, video.id)
            startService(this)
        }

    }

    private fun setClickListeners(video : Video) {
        btnLike.setOnClickListener {
            Intent(this, VideoService::class.java).apply {
                action = videoDetailsViewModel.getVideoServiceContract().LIKE_VOTE_ACTION
                putExtra(videoDetailsViewModel.getVideoServiceContract().LIKE_VOTE_EXTRA, video.id)
                startService(this)
            }
            notify("I like it!")
            attTextViews()
        }

        btnDislike.setOnClickListener {
            Intent(this, VideoService::class.java).apply {
                action = videoDetailsViewModel.getVideoServiceContract().DISLIKE_VOTE_ACTION
                putExtra(videoDetailsViewModel.getVideoServiceContract().DISLIKE_VOTE_EXTRA, video.id)
                startService(this)
            }
            notify("I didn't like it :(")
            attTextViews()
        }
    }

    private fun setStreaming(video: Video) {
        val url = VideoDAO.URL + "/video/${video.id}"
        try {
            val uri = Uri.parse(url)
            videoView.setMediaController(MediaController(this@VideoDetailsActivity))
            videoView.setVideoURI(uri)
        } catch (e:Exception) {
            notify(e.toString())
        }
    }

    private fun notify(string: String) {
        Snackbar.make(
            findViewById(R.id.layout_details),
            string,
            Snackbar.LENGTH_LONG
        ).show()
    }

    private fun bindComponents() {
        videoView   = findViewById(R.id.details_video)
        btnLike     = findViewById(R.id.details_btnLike)
        btnDislike  = findViewById(R.id.details_btnDislike)
        txtSource   = findViewById(R.id.details_source)
        txtSpeaker  = findViewById(R.id.details_speaker)
        txtLikes    = findViewById(R.id.details_likes)
        txtTitle    = findViewById(R.id.details_title)
        txtDuration = findViewById(R.id.details_duration)
        txtDislikes = findViewById(R.id.details_dislikes)
        txtViews    = findViewById(R.id.details_views)
        txtRecorded = findViewById(R.id.details_recorded)
        txtEvent    = findViewById(R.id.details_event)
    }

    private fun attTextViews() {
        Intent(this, VideoService::class.java).apply {
            action = videoDetailsViewModel.getVideoServiceContract().ATT_INFORMATION_ACTION
            putExtra(videoDetailsViewModel.getVideoServiceContract().ATT_INFORMATION_EXTRA, clickedId)
            startService(this)
        }
    }

    override fun onStop() {
        super.onStop()
        App.unregisterBroadcast(attVideoInformation)
    }
}
