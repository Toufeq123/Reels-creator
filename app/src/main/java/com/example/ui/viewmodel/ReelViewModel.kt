package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.ReelProjectEntity
import com.example.data.model.FramingMode
import com.example.data.model.ReelClip
import com.example.data.model.ReelCustomization
import com.example.data.model.YouTubeVideoInfo
import com.example.data.remote.GeminiReelAnalyzer
import com.example.data.remote.YouTubeApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class StudioTab(val labelBn: String, val labelEn: String) {
    STUDIO("রিল মেকার", "Reel Maker"),
    SAVED("সংরক্ষিত রিল", "Saved Reels"),
    TEMPLATES("ভাইরাল টেমপ্লেট", "Templates")
}

data class SampleVideo(
    val titleBn: String,
    val titleEn: String,
    val url: String,
    val category: String
)

class ReelViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val dao = db.reelProjectDao()

    val savedProjects: StateFlow<List<ReelProjectEntity>> = dao.getAllReels()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _activeTab = MutableStateFlow(StudioTab.STUDIO)
    val activeTab: StateFlow<StudioTab> = _activeTab.asStateFlow()

    private val _urlInput = MutableStateFlow("")
    val urlInput: StateFlow<String> = _urlInput.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _videoInfo = MutableStateFlow<YouTubeVideoInfo?>(null)
    val videoInfo: StateFlow<YouTubeVideoInfo?> = _videoInfo.asStateFlow()

    private val _reelClips = MutableStateFlow<List<ReelClip>>(emptyList())
    val reelClips: StateFlow<List<ReelClip>> = _reelClips.asStateFlow()

    private val _selectedReel = MutableStateFlow<ReelClip?>(null)
    val selectedReel: StateFlow<ReelClip?> = _selectedReel.asStateFlow()

    private val _customization = MutableStateFlow(ReelCustomization())
    val customization: StateFlow<ReelCustomization> = _customization.asStateFlow()

    private val _saveSuccessMessage = MutableStateFlow<String?>(null)
    val saveSuccessMessage: StateFlow<String?> = _saveSuccessMessage.asStateFlow()

    val sampleVideos = listOf(
        SampleVideo(
            titleBn = "টেক ও গ্যাজেট রিভিউ",
            titleEn = "Smartphone Tech Review",
            url = "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
            category = "📱 Tech"
        ),
        SampleVideo(
            titleBn = "পডকাস্ট মাইন্ডসেট টিপস",
            titleEn = "Life Changing Podcast",
            url = "https://www.youtube.com/watch?v=7h1s9v1-b0k",
            category = "🎙️ Podcast"
        ),
        SampleVideo(
            titleBn = "স্পেস ও বিজ্ঞান রহস্য",
            titleEn = "Universe Mysteries",
            url = "https://www.youtube.com/watch?v=libKVRa01L8",
            category = "🚀 Science"
        ),
        SampleVideo(
            titleBn = "মোটিভেশনাল স্পিচ",
            titleEn = "Daily Motivation Speech",
            url = "https://www.youtube.com/watch?v=ZXsQAXx_ao0",
            category = "🔥 Motivation"
        )
    )

    init {
        // Pre-load default sample video so user immediately sees a live rich studio on first launch
        loadSampleVideo(sampleVideos[0].url)
    }

    fun setTab(tab: StudioTab) {
        _activeTab.value = tab
    }

    fun onUrlChanged(newUrl: String) {
        _urlInput.value = newUrl
        _errorMessage.value = null
    }

    fun clearUrl() {
        _urlInput.value = ""
        _errorMessage.value = null
    }

    fun loadSampleVideo(url: String) {
        _urlInput.value = url
        processYouTubeUrl(url)
    }

    fun processYouTubeUrl(customUrl: String? = null) {
        val target = customUrl ?: _urlInput.value
        if (target.isBlank()) {
            _errorMessage.value = "অনুগ্রহ করে একটি ইউটিউব ভিডিও লিংক দিন (Please enter a YouTube URL)"
            return
        }

        val videoId = YouTubeApiClient.extractVideoId(target)
        if (videoId == null) {
            _errorMessage.value = "সঠিক ইউটিউব লিংক পাওয়া যায়নি। যেমন: youtube.com/watch?v=... বা youtu.be/..."
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val details = YouTubeApiClient.fetchVideoDetails(videoId, target)
                _videoInfo.value = details

                val timestamp = YouTubeApiClient.extractTimestampSeconds(target)

                val reels = GeminiReelAnalyzer.analyzeAndGenerateReels(details)
                _reelClips.value = reels

                // Select the first reel or one matching the timestamp
                val initialReel = if (timestamp != null && timestamp > 0) {
                    reels.firstOrNull()?.copy(
                        startTimeSec = timestamp,
                        endTimeSec = timestamp + 30
                    ) ?: reels.firstOrNull()
                } else {
                    reels.firstOrNull()
                }

                _selectedReel.value = initialReel
                initialReel?.let {
                    _customization.value = _customization.value.copy(
                        topBannerText = it.hookText,
                        subtitleText = it.bengaliCaption.take(45) + "..."
                    )
                }
            } catch (e: Exception) {
                _errorMessage.value = "ভিডিও লোড করতে সমস্যা হয়েছে: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun selectReel(reel: ReelClip) {
        _selectedReel.value = reel
        _customization.value = _customization.value.copy(
            topBannerText = reel.hookText,
            subtitleText = reel.bengaliCaption.take(45) + "...",
            stickerEmoji = reel.stickerEmoji
        )
    }

    fun updateStartTime(newStartSec: Int) {
        val current = _selectedReel.value ?: return
        val clampedStart = newStartSec.coerceAtLeast(0)
        val clampedEnd = if (clampedStart >= current.endTimeSec) clampedStart + 15 else current.endTimeSec
        _selectedReel.value = current.copy(
            startTimeSec = clampedStart,
            endTimeSec = clampedEnd
        )
    }

    fun updateEndTime(newEndSec: Int) {
        val current = _selectedReel.value ?: return
        val clampedEnd = newEndSec.coerceAtLeast(current.startTimeSec + 5)
        _selectedReel.value = current.copy(endTimeSec = clampedEnd)
    }

    fun setQuickDuration(seconds: Int) {
        val current = _selectedReel.value ?: return
        _selectedReel.value = current.copy(
            endTimeSec = current.startTimeSec + seconds
        )
    }

    fun updateFramingMode(mode: FramingMode) {
        _customization.value = _customization.value.copy(framingMode = mode)
    }

    fun updateBannerColor(hex: String) {
        _customization.value = _customization.value.copy(bannerColorHex = hex)
    }

    fun updateTopBannerText(text: String) {
        _customization.value = _customization.value.copy(topBannerText = text)
    }

    fun updateSubtitleText(text: String) {
        _customization.value = _customization.value.copy(subtitleText = text)
    }

    fun updateWatermark(handle: String) {
        _customization.value = _customization.value.copy(watermarkHandle = handle)
    }

    fun toggleWatermark(show: Boolean) {
        _customization.value = _customization.value.copy(showWatermark = show)
    }

    fun setStickerEmoji(emoji: String) {
        _customization.value = _customization.value.copy(stickerEmoji = emoji)
    }

    fun saveCurrentReel() {
        val video = _videoInfo.value ?: return
        val reel = _selectedReel.value ?: return
        val custom = _customization.value

        viewModelScope.launch {
            val entity = ReelProjectEntity(
                youtubeUrl = video.originalUrl,
                videoId = video.videoId,
                videoTitle = video.title,
                channelName = video.authorName,
                thumbnailUrl = video.thumbnailUrl,
                startTimeSec = reel.startTimeSec,
                endTimeSec = reel.endTimeSec,
                hookText = custom.topBannerText.ifBlank { reel.hookText },
                captionText = reel.bengaliCaption,
                hashtags = reel.hashtags.joinToString(" "),
                viralScore = reel.viralScore,
                framingMode = custom.framingMode.name,
                bannerText = custom.topBannerText,
                bannerColorHex = custom.bannerColorHex
            )
            dao.insertReel(entity)
            _saveSuccessMessage.value = "রিলটি 'সংরক্ষিত রিল' এ সফলভাবে সেভ করা হয়েছে! 🎉"
        }
    }

    fun clearSaveSuccessMessage() {
        _saveSuccessMessage.value = null
    }

    fun deleteSavedReel(id: Long) {
        viewModelScope.launch {
            dao.deleteById(id)
        }
    }

    fun applyTemplate(templateTitle: String, hook: String, framingMode: FramingMode, bannerColor: String, emoji: String) {
        val currentReel = _selectedReel.value ?: return
        _selectedReel.value = currentReel.copy(
            hookText = hook,
            stickerEmoji = emoji
        )
        _customization.value = _customization.value.copy(
            framingMode = framingMode,
            bannerColorHex = bannerColor,
            topBannerText = hook,
            stickerEmoji = emoji
        )
        _activeTab.value = StudioTab.STUDIO
    }
}
