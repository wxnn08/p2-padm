package com.wesley.videostreamingp2.model

import android.app.IntentService
import android.content.Intent
import android.util.Log
import com.wesley.videostreamingp2.App


object VideoServiceContract {
    // comandos
    const val THUMBNAIL_DOWNLOAD_ACTION = "thumnailDownloadAction"
    const val THUMBNAIL_DOWNLOAD_EXTRA = "thumbnailDownloadExtra"
    const val RECENTS_DOWNLOAD_ACTION = "recentsDownloadAction"

    // broadcasts
    const val RECENTS_LIST_READY = "recentsListReady"
        const val RECENTS_LIST_READY_EXTRA = "recentsListReadyExtra"

    const val THUMBNAIL_READY = "thumbnailReady"
    const val THUMBNAIL_READY_EXTRA = "thumbnailReadyExtra"

}

class VideoService : IntentService("VideoService") {

    override fun onHandleIntent(intent: Intent?) {
        Log.d("ALLINFORMATION", "Service")
        if(App.isNetworkConnected()) {
            when(intent?.action) {
                VideoServiceContract.RECENTS_DOWNLOAD_ACTION -> {
                    Log.d("ALLINFORMATION", "Service DownloadAction")
                    downloadRecents()
                }
                VideoServiceContract.THUMBNAIL_DOWNLOAD_ACTION -> {
                    intent.takeIf {
                        it.hasExtra(VideoServiceContract.THUMBNAIL_DOWNLOAD_EXTRA)
                    }?.apply {
                        downloadThumbnail(intent.getSerializableExtra(
                            VideoServiceContract.THUMBNAIL_DOWNLOAD_EXTRA) as Video)
                    }
                }
            }
        }
    }

    private fun downloadRecents() {
        Intent(VideoServiceContract.RECENTS_LIST_READY).apply {
            putExtra(VideoServiceContract.RECENTS_LIST_READY_EXTRA, VideoFetcher.fetchRecentVideos())
            App.sendBroadcast(this)
        }
    }

    private fun downloadThumbnail(video: Video) {
        val resultThumb = VideoThumbDownloader.download(video)

        Intent(VideoServiceContract.THUMBNAIL_READY).apply {
            putExtra(VideoServiceContract.THUMBNAIL_READY_EXTRA, resultThumb)
            App.sendBroadcast(this)
        }
    }

}