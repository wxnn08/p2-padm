package com.wesley.videostreamingp2.model

import android.util.Log
import com.wesley.videostreamingp2.App
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

object VideoThumbDownloader {
    fun download(video: Video): Video {
        val localPhoto = video.copy()

        try {
            Log.d("ALLINFORMATION", "Tentando acessar thumb ${video.id}")
            val url = URL("http://192.168.15.14:8099/thumbnail/${video.id}")
            val conn = url.openConnection() as HttpURLConnection
            conn.connect()

            val responseCode = conn.responseCode
            if(responseCode == HttpURLConnection.HTTP_OK) {
                val outputFile = File(App.appContext.cacheDir, video.id.toString())
                val outputStream = FileOutputStream(outputFile)
                conn.inputStream.copyTo(outputStream)
                localPhoto.thumbFile = outputFile
                outputStream.close()
            } else {
                Log.d("ALLINFORMATIONS", "DEU MERDA")
            }
        } catch (e: MalformedURLException) {
            Log.d("ALLINFORMATIONS", e.toString())
        } catch (e: IOException) {
            Log.d("ALLINFORMATIONS", e.toString())
        }
        return localPhoto
    }

    //TODO(implementar notify)

}
