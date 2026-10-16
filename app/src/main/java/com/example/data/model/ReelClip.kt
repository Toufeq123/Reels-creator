package com.example.data.model

data class ReelClip(
    val id: String,
    val title: String,
    val hookText: String,
    val startTimeSec: Int,
    val endTimeSec: Int,
    val bengaliCaption: String,
    val englishCaption: String,
    val hashtags: List<String>,
    val viralScore: Int, // 1 to 100
    val categoryTag: String, // e.g. "🔥 হাইপ ইন্ট্রো", "💡 মূল কথা", "😂 মজার অংশ"
    val stickerEmoji: String = "🔥"
) {
    val durationSec: Int
        get() = (endTimeSec - startTimeSec).coerceAtLeast(1)

    fun formattedTimeRange(): String {
        val startM = startTimeSec / 60
        val startS = startTimeSec % 60
        val endM = endTimeSec / 60
        val endS = endTimeSec % 60
        return "%02d:%02d - %02d:%02d (%ds)".format(startM, startS, endM, endS, durationSec)
    }

    fun fullShareText(videoUrl: String): String {
        val tagsString = hashtags.joinToString(" ")
        return """
🎬 $hookText

$bengaliCaption

🕒 ক্লিপ টাইমস্ট্যাম্প: ${formattedTimeRange()}
🔗 ভিডিও লিংক: $videoUrl?t=${startTimeSec}s

$tagsString
#ReelSnap #YouTubeToReels
        """.trimIndent()
    }
}

enum class FramingMode(val labelBn: String, val labelEn: String) {
    BLUR_BACKGROUND("ব্লার ব্যাকগ্রাউন্ড", "Blur Background"),
    CROP_FILL("ক্রপ ও জুম (৯:১৬)", "Crop & Fill (9:16)"),
    TOP_BOTTOM_BAR("ব্যানার বার", "Header/Footer Bars"),
    LETTERBOX("ফুল ফ্রেম", "Letterbox")
}

data class ReelCustomization(
    val framingMode: FramingMode = FramingMode.BLUR_BACKGROUND,
    val bannerColorHex: String = "#FF0050",
    val topBannerText: String = "WATCH TILL END 😱",
    val subtitleText: String = "",
    val watermarkHandle: String = "@mychannel",
    val showWatermark: Boolean = true,
    val showSticker: Boolean = true,
    val stickerEmoji: String = "🔥"
)
