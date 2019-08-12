package com.wesley.videostreamingp2.viewmodel

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.MediaController
import android.widget.VideoView
import androidx.lifecycle.ViewModel
import com.wesley.videostreamingp2.model.Video
import com.wesley.videostreamingp2.model.VideoDAO
import com.wesley.videostreamingp2.model.VideoService
import com.wesley.videostreamingp2.model.VideoServiceContract
import com.wesley.videostreamingp2.view.VideoDetailsActivity

class VideoDetailsViewModel : ViewModel() {

    var incrementOk : Boolean = false
    var videoTime : Int = 0

    fun getVideoAt(pos: Int): Video {
        return VideoDAO.getVideoAt(pos)
    }

    fun getVideoServiceContract() : VideoServiceContract{
        return VideoServiceContract
    }

    fun setStreaming(video: Video, videoView: VideoView, videoDetailsActivity: VideoDetailsActivity) {
        val url = VideoDAO.URL + "/video/${video.id}"
        val uri = Uri.parse(url)
        videoView.setMediaController(MediaController(videoDetailsActivity))
        videoView.setVideoURI(uri)
        videoView.setOnPreparedListener {
            it.seekTo(videoTime)
        }
    }

    override fun onCleared() {
        super.onCleared()


    }

    fun incrementView(video: Video, videoDetailsActivity: VideoDetailsActivity) {
        if(!incrementOk) {
            Intent(videoDetailsActivity, VideoService::class.java).apply {
                action = getVideoServiceContract().VIEW_INCREMENT_ACTION
                putExtra(getVideoServiceContract().VIEW_INCREMENT_EXTRA, video.id)
                videoDetailsActivity.startService(this)
            }
            incrementOk = true
        }
    }

}