package com.wesley.videostreamingp2.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.wesley.videostreamingp2.App
import com.wesley.videostreamingp2.R
import com.wesley.videostreamingp2.model.Video
import com.wesley.videostreamingp2.model.VideoList
import com.wesley.videostreamingp2.model.VideoService
import com.wesley.videostreamingp2.model.VideoServiceContract
import kotlinx.android.synthetic.main.list_video.view.*

class VideoAdapter(private val mainActivity: MainActivity) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    var videoList = VideoList()

    inner class VideoViewHolder (view: View): RecyclerView.ViewHolder(view) {

        val thumbnail = view.thumbnail
        val titulo = view.titulo
        val data = view.data
        val visualizacoes = view.visualizacoes
        lateinit var video: Video
        // Pode adicionar botões aqui (com click listener)

        val thumbnailReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val self = this
                intent?.takeIf { it.hasExtra(VideoServiceContract.THUMBNAIL_READY_EXTRA)}?.apply {
                    val videoRecebido = intent.getSerializableExtra(VideoServiceContract.THUMBNAIL_READY_EXTRA) as Video
                    Log.d("ALLINFORMATION","ViewHolder 1")
                    if(videoRecebido.hastThumbFile() && videoRecebido.id == video.id) {
                        Log.d("ALLINFORMATION","ViewHolder 2")
                        thumbnail.setImageBitmap(BitmapFactory.decodeStream(videoRecebido.thumbFile.inputStream()))
                        //thumbnail.layoutParams.width = ConstraintLayout.LayoutParams.MATCH_PARENT
                        //thumbnail.layoutParams.height = ConstraintLayout.LayoutParams.WRAP_CONTENT
                        App.unregisterBroadcast(self)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        return VideoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_video, parent, false))
    }

    override fun getItemCount(): Int {
        return videoList.size()
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val video = videoList.getVideoAt(position)
        holder.titulo.text = video.titulo
        holder.data.text = video.data
        holder.visualizacoes.text = video.visualizacoes.toString()
        holder.video = video

        App.registerBroadcast(holder.thumbnailReceiver, IntentFilter(VideoServiceContract.THUMBNAIL_READY))

        Intent(mainActivity, VideoService::class.java).apply {
            action = VideoServiceContract.THUMBNAIL_DOWNLOAD_ACTION
            putExtra(VideoServiceContract.THUMBNAIL_DOWNLOAD_EXTRA, video)
            mainActivity.startService(this)
            Log.d("ALLINFORMATION", "Adapter->service thumb download")
        }

        //Picasso.get().load(url).into(holder.thumbnail)
        //holder.thumbnail.adjustViewBounds = true
    }

}
