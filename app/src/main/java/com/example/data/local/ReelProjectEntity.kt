package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reel_projects")
data class ReelProjectEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val youtubeUrl: String,
    val videoId: String,
    val videoTitle: String,
    val channelName: String,
    val thumbnailUrl: String,
    val startTimeSec: Int,
    val endTimeSec: Int,
    val hookText: String,
    val captionText: String,
    val hashtags: String,
    val viralScore: Int,
    val framingMode: String,
    val bannerText: String,
    val bannerColorHex: String,
    val createdAt: Long = System.currentTimeMillis()
)
