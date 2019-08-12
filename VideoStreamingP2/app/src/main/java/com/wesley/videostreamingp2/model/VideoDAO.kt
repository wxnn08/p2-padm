package com.wesley.videostreamingp2.model

object VideoDAO {
    const val URL : String = "http://192.168.15.12:8099"

    var videos = VideoList()

    fun add(video: Video) {
        videos.add(video)
    }

    fun getVideoAt(position: Int) : Video {
        return videos.getVideoAt(position)
    }

    fun size(): Int {
        return videos.size()
    }
}