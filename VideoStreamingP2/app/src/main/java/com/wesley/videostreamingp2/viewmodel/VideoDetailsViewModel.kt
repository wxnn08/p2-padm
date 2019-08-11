package com.wesley.videostreamingp2.viewmodel

import androidx.lifecycle.ViewModel
import com.wesley.videostreamingp2.model.Video
import com.wesley.videostreamingp2.model.VideoDAO
import com.wesley.videostreamingp2.model.VideoServiceContract

class VideoDetailsViewModel : ViewModel() {
    fun getVideoAt(pos: Int): Video {
        return VideoDAO.getVideoAt(pos)
    }

    fun getVideoServiceContract() : VideoServiceContract{
        return VideoServiceContract
    }

}