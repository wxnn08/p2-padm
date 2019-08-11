package com.wesley.videostreamingp2.viewmodel

import androidx.lifecycle.ViewModel
import com.wesley.videostreamingp2.model.VideoServiceContract

class MainViewModel : ViewModel() {

    fun getVideoServiceContract() : VideoServiceContract {
        return VideoServiceContract
    }
}