package com.wesley.videostreamingp2.model

import java.io.File

object VideoFetcher {
    fun fetchRecentVideos(): VideoList {
        val videos = VideoList()
        videos.add(Video("aaa",
            File(""),
            "Wesley",
            10,
            "TESTANDO",
            "10:10",
            5,
            100,
            "Mar 15",
            1,
            "TED 2015"))

        videos.add(Video("aaa",
            File(""),
            "Wesley",
            10,
            "aaaTESTANDOaaa",
            "10:10",
            5,
            1112,
            "Dec 15",
            2,
            "TED 2015"))

        return videos
    }
}
