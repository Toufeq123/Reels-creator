package com.example

import com.example.data.model.ReelClip
import com.example.data.model.YouTubeVideoInfo
import com.example.data.remote.GeminiReelAnalyzer
import com.example.data.remote.YouTubeApiClient
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class YouTubeVideoExtractorTest {

    @Test
    fun testExtractVideoIdFromStandardWatchUrl() {
        val url = "https://www.youtube.com/watch?v=dQw4w9WgXcQ"
        val id = YouTubeApiClient.extractVideoId(url)
        assertEquals("dQw4w9WgXcQ", id)
    }

    @Test
    fun testExtractVideoIdFromShortUrl() {
        val url = "https://youtu.be/dQw4w9WgXcQ"
        val id = YouTubeApiClient.extractVideoId(url)
        assertEquals("dQw4w9WgXcQ", id)
    }

    @Test
    fun testExtractVideoIdFromShortsUrl() {
        val url = "https://www.youtube.com/shorts/abc123xyz78"
        val id = YouTubeApiClient.extractVideoId(url)
        assertEquals("abc123xyz78", id)
    }

    @Test
    fun testExtractTimestamp() {
        val urlWithTimestamp = "https://www.youtube.com/watch?v=dQw4w9WgXcQ&t=45s"
        val seconds = YouTubeApiClient.extractTimestampSeconds(urlWithTimestamp)
        assertEquals(45, seconds)
    }

    @Test
    fun testReelClipDurationAndFormatting() {
        val clip = ReelClip(
            id = "test-1",
            title = "Test Hook",
            hookText = "Mind-blowing trick!",
            startTimeSec = 15,
            endTimeSec = 45,
            bengaliCaption = "দারুণ ভিডিও",
            englishCaption = "Great video",
            hashtags = listOf("#Shorts", "#Reels"),
            viralScore = 95,
            categoryTag = "🔥 হাইপ"
        )
        assertEquals(30, clip.durationSec)
        assertEquals("00:15 - 00:45 (30s)", clip.formattedTimeRange())
    }

    @Test
    fun testSmartHeuristicReelsGeneration() {
        val video = YouTubeVideoInfo(
            videoId = "test1234567",
            originalUrl = "https://youtu.be/test1234567",
            title = "বাংলা সেরা টেক রিভিউ ২০২৬",
            authorName = "Tech Bangla",
            thumbnailUrl = "https://img.youtube.com/vi/test1234567/hqdefault.jpg",
            highResThumbnailUrl = "https://img.youtube.com/vi/test1234567/maxresdefault.jpg"
        )
        val reels = GeminiReelAnalyzer.generateSmartHeuristicReels(video)
        assertTrue(reels.isNotEmpty())
        assertEquals(4, reels.size)
        assertTrue(reels.first().viralScore in 80..100)
    }
}
