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
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.wesley.videostreamingp2.App
import com.wesley.videostreamingp2.R
import com.wesley.videostreamingp2.model.Video
import com.wesley.videostreamingp2.model.VideoList
import com.wesley.videostreamingp2.model.VideoService
import kotlinx.android.synthetic.main.list_video.view.*

class VideoAdapter(private val mainActivity: MainActivity) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    var videoList = VideoList()

    inner class VideoViewHolder (view: View): RecyclerView.ViewHolder(view) {

        val thumbnail = view.thumbnail
        val titulo = view.titulo
        val data = view.data
        val visualizacoes = view.visualizacoes
        lateinit var video: Video

        val thumbnailReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val self = this
                intent?.takeIf { it.hasExtra(mainActivity.mainViewModel.getVideoServiceContract().THUMBNAIL_READY_EXTRA)}?.apply {
                    val videoRecebido = intent.getSerializableExtra(mainActivity.mainViewModel.getVideoServiceContract().THUMBNAIL_READY_EXTRA) as Video
                    Log.d("ALLINFORMATION","ViewHolder 1")
                    if(videoRecebido.hastThumbFile() && videoRecebido.id == video.id) {
                        Log.d("ALLINFORMATION","ViewHolder 2")
                        thumbnail.setImageBitmap(BitmapFactory.decodeStream(videoRecebido.thumbFile.inputStream()))
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
        holder.titulo.text = video.title
        holder.data.text = video.recorded
        holder.visualizacoes.text = video.views.toString()
        holder.video = video

        App.registerBroadcast(holder.thumbnailReceiver, IntentFilter(mainActivity.mainViewModel.getVideoServiceContract().THUMBNAIL_READY))

        Intent(mainActivity, VideoService::class.java).apply {
            action = mainActivity.mainViewModel.getVideoServiceContract().THUMBNAIL_DOWNLOAD_ACTION
            putExtra(mainActivity.mainViewModel.getVideoServiceContract().THUMBNAIL_DOWNLOAD_EXTRA, video)
            mainActivity.startService(this)
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, VideoDetailsActivity::class.java).apply {
                putExtra("posicao", position)
            }
            ContextCompat.startActivity(it.context, intent, null)
        }
    }

}
