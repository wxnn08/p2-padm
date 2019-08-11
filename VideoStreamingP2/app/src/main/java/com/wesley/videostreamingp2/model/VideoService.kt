package com.wesley.videostreamingp2.model

import android.app.IntentService
import android.content.Intent
import android.util.Log
import com.wesley.videostreamingp2.App
import java.net.HttpURLConnection
import java.net.URL


object VideoServiceContract {
    // comandos
    const val VIEW_INCREMENT_ACTION = "viewIncrementAction"
    const val VIEW_INCREMENT_EXTRA = "viewIncrementExtra"

    const val LIKE_VOTE_ACTION = "likeVoteAction"
    const val LIKE_VOTE_EXTRA = "likeVoteExtra"

    const val DISLIKE_VOTE_ACTION = "dislikeVoteAction"
    const val DISLIKE_VOTE_EXTRA = "dislikeVoteExtra"

    const val THUMBNAIL_DOWNLOAD_ACTION = "thumnailDownloadAction"
    const val THUMBNAIL_DOWNLOAD_EXTRA = "thumbnailDownloadExtra"

    const val RECENTS_DOWNLOAD_ACTION = "recentsDownloadAction"

    const val ATT_INFORMATION_ACTION = "attInformationAction"
    const val ATT_INFORMATION_EXTRA = "attInformationExtra"

    // broadcasts
    const val RECENTS_LIST_READY = "recentsListReady"
    const val RECENTS_LIST_READY_EXTRA = "recentsListReadyExtra"

    const val THUMBNAIL_READY = "thumbnailReady"
    const val THUMBNAIL_READY_EXTRA = "thumbnailReadyExtra"

    const val VIDEO_INFORMATION = "videoInformation"
    const val VIDEO_INFORMATION_EXTRA = "videoInformationExtra"

    const val NOTIFY_ERROR = "notifyError"
    const val NOTIFY_ERROR_EXTRA = "notifyErrorExtra"
}

class VideoService : IntentService("VideoService") {

    override fun onHandleIntent(intent: Intent?) {
        Log.d("ALLINFORMATION", "Service iniciado")
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
                VideoServiceContract.LIKE_VOTE_ACTION -> {
                    incrementLike(intent)
                }
                VideoServiceContract.DISLIKE_VOTE_ACTION -> {
                    incrementDislike(intent)
                }
                VideoServiceContract.VIEW_INCREMENT_ACTION -> {
                    incrementView(intent)
                }
                VideoServiceContract.ATT_INFORMATION_ACTION -> {
                    attVideoInformation(intent)
                }
            }
        }
    }

    private fun attVideoInformation(intent: Intent) {
        intent.takeIf { it.hasExtra(VideoServiceContract.ATT_INFORMATION_EXTRA) }.apply {
            val idVideo = intent.getIntExtra(VideoServiceContract.ATT_INFORMATION_EXTRA, -1)
            if(idVideo != -1) {
                downloadRecents()
                Intent(VideoServiceContract.VIDEO_INFORMATION).apply {
                    putExtra(VideoServiceContract.VIDEO_INFORMATION_EXTRA, VideoDAO.getVideoAt(idVideo))
                    App.sendBroadcast(this)
                }
            } else {
                Log.d("ALLINFORMATION", "ERRO NA ATT")
            }
        }
    }

    private fun incrementView(intent: Intent) {
        intent.takeIf { it.hasExtra(VideoServiceContract.VIEW_INCREMENT_EXTRA) }?.apply {
            val idVideo = intent.getIntExtra(VideoServiceContract.VIEW_INCREMENT_EXTRA, -1)
            if(idVideo != -1) {
                val url = URL(VideoDAO.URL + "/view/$idVideo")
                Log.d("ALLINFORMATION", VideoDAO.URL + "/view/$idVideo")
                val conn = url.openConnection() as HttpURLConnection
                if (conn.responseCode == HttpURLConnection.HTTP_OK) {
                    conn.apply {
                        requestMethod = "GET"
                        connect()
                        inputStream
                    }
                }
            } else {
                Log.d("ALLINFORMATION", "ERRO NO DISLIKE")
            }
        }
    }

    private fun incrementDislike(intent: Intent) {
        intent.takeIf { it.hasExtra(VideoServiceContract.DISLIKE_VOTE_EXTRA) }?.apply {
            val idVideo = intent.getIntExtra(VideoServiceContract.DISLIKE_VOTE_EXTRA, -1)
            if(idVideo != -1) {
                val url = URL(VideoDAO.URL + "/dislike/$idVideo")
                Log.d("ALLINFORMATION", VideoDAO.URL + "/dislike/$idVideo")
                val conn = url.openConnection() as HttpURLConnection
                if(conn.responseCode == HttpURLConnection.HTTP_OK) {
                    conn.apply {
                        requestMethod = "GET"
                        connect()
                        inputStream
                    }
                }
            } else {
                Log.d("ALLINFORMATION", "ERRO NO DISLIKE")
            }
        }
    }

    private fun incrementLike(intent: Intent) {
        intent.takeIf {
            it.hasExtra(VideoServiceContract.LIKE_VOTE_EXTRA)
        }?.apply {
            val idVideo = intent.getIntExtra(VideoServiceContract.LIKE_VOTE_EXTRA, -1)
            if(idVideo != -1) {
                val url = URL(VideoDAO.URL + "/like/$idVideo")
                Log.d("ALLINFORMATION", VideoDAO.URL + "/like/$idVideo")
                val conn = url.openConnection() as HttpURLConnection
                if (conn.responseCode == HttpURLConnection.HTTP_OK) {
                    conn.apply {
                        requestMethod = "GET"
                        connect()
                        inputStream
                    }
                }
            } else {
                Log.d("ALLINFORMATION", "ERRO NO LIKE")
            }
        }
    }

    private fun downloadRecents() {
        Intent(VideoServiceContract.RECENTS_LIST_READY).apply {
            putExtra(VideoServiceContract.RECENTS_LIST_READY_EXTRA, VideoDownloader.downloadList())
            App.sendBroadcast(this)
        }
    }

    private fun downloadThumbnail(video: Video) {
        val resultThumb = VideoDownloader.downloadThumbnail(video)

        Intent(VideoServiceContract.THUMBNAIL_READY).apply {
            putExtra(VideoServiceContract.THUMBNAIL_READY_EXTRA, resultThumb)
            App.sendBroadcast(this)
        }
    }

}