package com.example.data.model

data class YouTubeVideoInfo(
    val videoId: String,
    val originalUrl: String,
    val title: String,
    val authorName: String,
    val thumbnailUrl: String,
    val highResThumbnailUrl: String,
    val approxDurationSec: Int = 180
) {
    val embedUrl: String
        get() = "https://www.youtube.com/embed/$videoId?autoplay=1&playsinline=1&controls=1&modestbranding=1&rel=0"
}
