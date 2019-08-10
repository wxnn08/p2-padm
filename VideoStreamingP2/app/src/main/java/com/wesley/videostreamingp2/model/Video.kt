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
    var url: String,
    var thumbFile: File = File(""),
    var palestrante: String,
    var likes: Int,
    var titulo : String,
    var duracao: String,
    var dislikes: Int,
    var visualizacoes : Int,
    var data: String,
    var id: Int,
    var evento: String ) : Serializable {

    fun hastThumbFile(): Boolean {
        return thumbFile.name.trim().isNotEmpty()
    }

}