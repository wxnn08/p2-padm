package com.wesley.videostreamingp2.model

import android.content.Intent
import android.util.Log
import com.wesley.videostreamingp2.App
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

object VideoDownloader {
    fun downloadList(): VideoList {

        var jsonList = ""
        try {
            val url = URL(VideoDAO.URL + "/videos")
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "GET"
            conn.connect()

            if(conn.responseCode == HttpURLConnection.HTTP_OK) {
                jsonList = conn.inputStream.use {
                    it.reader().use {reader ->
                        reader.readText()
                    }
                }
            }

            conn.disconnect()

            val jsonArray = JSONObject(jsonList).getJSONArray("videos")
            val list = VideoList()

            var objId = 0
            while (objId < jsonArray.length()){
                val jsonObject = jsonArray.getJSONObject(objId++)
                list.add(
                    Video(
                        jsonObject.getString("source"),
                        File(""),
                        jsonObject.getString("speaker"),
                        jsonObject.getInt("likes"),
                        jsonObject.getString("title"),
                        jsonObject.getString("duration"),
                        jsonObject.getInt("dislikes"),
                        jsonObject.getInt("views"),
                        jsonObject.getString("recorded"),
                        jsonObject.getInt("id"),
                        jsonObject.getString("event")
                    )
                )
            }

            VideoDAO.videos = list

        } catch (e: MalformedURLException) {
            sendStatus(e.toString())
        } catch (e: IOException) {
            sendStatus(e.toString())
        }

        return VideoDAO.videos
    }


    fun downloadThumbnail(video: Video): Video {
        val thumbLocal = video.copy()

        try {
            val url = URL(VideoDAO.URL + "/thumbnail/${video.id}")
            val conn = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                connect()
            }

            if(conn.responseCode == HttpURLConnection.HTTP_OK) {
                val outputFile = File(App.appContext.cacheDir, video.id.toString())
                val outputStream = FileOutputStream(outputFile)
                conn.inputStream.copyTo(outputStream)
                thumbLocal.thumbFile = outputFile
                outputStream.close()
            }
            conn.disconnect()

        } catch (e: MalformedURLException) {
            sendStatus(e.toString())
        } catch (e: IOException) {
            sendStatus(e.toString())
        }
        return thumbLocal
    }

    private fun sendStatus(string: String) {
        Intent(VideoServiceContract.NOTIFY_ERROR).apply {
            putExtra(VideoServiceContract.NOTIFY_ERROR_EXTRA, string)
            App.sendBroadcast(this)
        }
    }
}