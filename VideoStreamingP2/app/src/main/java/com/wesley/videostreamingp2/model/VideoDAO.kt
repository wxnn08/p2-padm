package com.wesley.videostreamingp2.model

object VideoDAO {
    val videos : MutableList<Video> = ArrayList()

    fun add(video: Video) {
        videos.add(video)
    }

    fun getVideoAt(position: Int) : Video {
        return videos[position]
    }

    fun size(): Int {
        return videos.size
    }
}