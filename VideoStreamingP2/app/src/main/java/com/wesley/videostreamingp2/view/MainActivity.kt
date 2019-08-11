package com.wesley.videostreamingp2.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.wesley.videostreamingp2.App
import com.wesley.videostreamingp2.viewmodel.MainViewModel
import com.wesley.videostreamingp2.R
import com.wesley.videostreamingp2.model.VideoList
import com.wesley.videostreamingp2.model.VideoService

class MainActivity : AppCompatActivity() {

    lateinit var mainViewModel: MainViewModel
    lateinit var filmeList: RecyclerView


    private val videoListReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.takeIf { it.hasExtra(mainViewModel.getVideoServiceContract().RECENTS_LIST_READY_EXTRA) }.apply {
                VideoAdapter(this@MainActivity).apply {
                    videoList = intent?.getSerializableExtra(mainViewModel.getVideoServiceContract().RECENTS_LIST_READY_EXTRA) as VideoList
                    filmeList.adapter = this
                    (filmeList.adapter as VideoAdapter).notifyDataSetChanged()
                }
            }
        }

    }

    private val errorListReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.takeIf { it.hasExtra(mainViewModel.getVideoServiceContract().NOTIFY_ERROR_EXTRA) }.apply {
                intent?.getStringExtra(mainViewModel.getVideoServiceContract().NOTIFY_ERROR_EXTRA)?.let {
                    notify(it)
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
    }

    override fun onStart() {
        super.onStart()

        App.registerBroadcast(videoListReceiver, IntentFilter(mainViewModel.getVideoServiceContract().RECENTS_LIST_READY))
        App.registerBroadcast(errorListReceiver, IntentFilter(mainViewModel.getVideoServiceContract().NOTIFY_ERROR))

        Intent(this, VideoService::class.java).apply {
            action = mainViewModel.getVideoServiceContract().RECENTS_DOWNLOAD_ACTION
            startService(this)
        }
    }

    override fun onStop() {
        super.onStop()
        App.unregisterBroadcast(videoListReceiver)
        App.unregisterBroadcast(errorListReceiver)
    }

    private fun notify (string: String) {
        Snackbar.make(
            findViewById(R.id.layout_main),
            string,
            Snackbar.LENGTH_LONG
        ).show()
    }
}
