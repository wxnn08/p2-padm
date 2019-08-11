package com.wesley.videostreamingp2.model

import java.io.File
import java.io.Serializable

class VideoList : Serializable {
    val videos : ArrayList<Video> = ArrayList()

    fun add (video: Video) = videos.add(video)
    fun remove (video: Video) = videos.remove(video)
    fun size() = videos.size
    fun getVideoAt(pos: Int) = videos[pos]
}

data class Video (
    var source: String,
    var thumbFile: File = File(""),
    var speaker: String,
    var likes: Int,
    var title : String,
    var duration: String,
    var dislikes: Int,
    var views : Int,
    var recorded: String,
    var id: Int,
    var event: String ) : Serializable {

    fun hastThumbFile(): Boolean {
        return thumbFile.name.trim().isNotEmpty()
    }

}