package com.example.data.remote

import com.example.data.model.YouTubeVideoInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

object YouTubeApiClient {

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    // Regex to match YouTube video ID from various link types
    private val YOUTUBE_ID_PATTERN = Pattern.compile(
        "(?:youtu\\.be\\/|youtube\\.com\\/(?:embed\\/|v\\/|shorts\\/|live\\/|watch\\?v=|watch\\?.+&v=))([\\w-]{11})",
        Pattern.CASE_INSENSITIVE
    )

    private val TIMESTAMP_PATTERN = Pattern.compile("(?:[?&]t=)(\\d+)(?:s)?")

    fun extractVideoId(rawUrlOrText: String): String? {
        val trimmed = rawUrlOrText.trim()
        // If user already typed/pasted just the 11 character video ID
        if (trimmed.length == 11 && trimmed.matches(Regex("^[a-zA-Z0-9_-]{11}$"))) {
            return trimmed
        }
        val matcher = YOUTUBE_ID_PATTERN.matcher(trimmed)
        return if (matcher.find()) matcher.group(1) else null
    }

    fun extractTimestampSeconds(rawUrl: String): Int? {
        val matcher = TIMESTAMP_PATTERN.matcher(rawUrl)
        return if (matcher.find()) {
            matcher.group(1)?.toIntOrNull()
        } else null
    }

    suspend fun fetchVideoDetails(videoId: String, originalUrl: String): YouTubeVideoInfo = withContext(Dispatchers.IO) {
        val standardUrl = "https://www.youtube.com/watch?v=$videoId"
        val oEmbedUrl = "https://www.youtube.com/oembed?url=$standardUrl&format=json"

        val defaultTitle = "YouTube Video ($videoId)"
        val defaultAuthor = "YouTube Creator"
        val highResThumb = "https://img.youtube.com/vi/$videoId/maxresdefault.jpg"
        val hqThumb = "https://img.youtube.com/vi/$videoId/hqdefault.jpg"

        try {
            val request = Request.Builder()
                .url(oEmbedUrl)
                .header("User-Agent", "Mozilla/5.0 (Android; Mobile; rv:109.0)")
                .build()

            val response = httpClient.newCall(request).execute()
            if (response.isSuccessful) {
                val bodyString = response.body?.string() ?: ""
                val json = JSONObject(bodyString)
                val title = json.optString("title", defaultTitle)
                val author = json.optString("author_name", defaultAuthor)
                val thumb = json.optString("thumbnail_url", highResThumb)

                return@withContext YouTubeVideoInfo(
                    videoId = videoId,
                    originalUrl = originalUrl,
                    title = title,
                    authorName = author,
                    thumbnailUrl = thumb,
                    highResThumbnailUrl = highResThumb,
                    approxDurationSec = 180
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Fallback info if offline or network error
        YouTubeVideoInfo(
            videoId = videoId,
            originalUrl = originalUrl,
            title = defaultTitle,
            authorName = defaultAuthor,
            thumbnailUrl = hqThumb,
            highResThumbnailUrl = highResThumb,
            approxDurationSec = 180
        )
    }
}
