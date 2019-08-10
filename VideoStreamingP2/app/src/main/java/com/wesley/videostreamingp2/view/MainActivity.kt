package com.wesley.videostreamingp2.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wesley.videostreamingp2.App
import com.wesley.videostreamingp2.viewmodel.MainViewModel
import com.wesley.videostreamingp2.R
import com.wesley.videostreamingp2.model.VideoList
import com.wesley.videostreamingp2.model.VideoService
import com.wesley.videostreamingp2.model.VideoServiceContract

class MainActivity : AppCompatActivity() {

    lateinit var mainViewModel: MainViewModel
    lateinit var filmeList: RecyclerView


    private val videoListReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d("ALLINFORMATION", "Main entrei broadcast videoListReceiver")
            intent?.takeIf { it.hasExtra(VideoServiceContract.RECENTS_LIST_READY_EXTRA) }.apply {
                VideoAdapter(this@MainActivity).apply {
                    videoList = intent?.getSerializableExtra(VideoServiceContract.RECENTS_LIST_READY_EXTRA) as VideoList
                    filmeList.adapter = this
                    (filmeList.adapter as VideoAdapter).notifyDataSetChanged()
                }
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        filmeList = findViewById(R.id.main_recycler_filmes)
        filmeList.layoutManager = LinearLayoutManager(App.appContext)
        filmeList.setHasFixedSize(true)

        Log.d("ALLINFORMATION", "Main criada")
    }

    override fun onStart() {
        super.onStart()

        App.registerBroadcast(videoListReceiver, IntentFilter(VideoServiceContract.RECENTS_LIST_READY))

        Intent(this, VideoService::class.java).apply {
            action = VideoServiceContract.RECENTS_DOWNLOAD_ACTION
            startService(this)
        }

        Log.d("ALLINFORMATION", "Main OnStart")
    }

    override fun onStop() {
        super.onStop()
        App.unregisterBroadcast(videoListReceiver)
    }
}
