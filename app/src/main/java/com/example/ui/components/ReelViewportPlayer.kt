package com.example.ui.components

import android.annotation.SuppressLint
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SmartDisplay
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.model.FramingMode
import com.example.data.model.ReelClip
import com.example.data.model.ReelCustomization
import com.example.data.model.YouTubeVideoInfo

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun ReelViewportPlayer(
    videoInfo: YouTubeVideoInfo,
    reelClip: ReelClip,
    customization: ReelCustomization,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isLiveWebPlayerActive by remember { mutableStateOf(false) }
    var webViewInstance by remember { mutableStateOf<WebView?>(null) }

    val bannerColor = try {
        Color(android.graphics.Color.parseColor(customization.bannerColorHex))
    } catch (e: Exception) {
        Color(0xFFFF0050)
    }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Toggle bar between Visual Reel Preview and Live Web Player
        Row(
            modifier = Modifier
                .padding(bottom = 8.dp)
                .background(Color(0xFF1E1B2E), RoundedCornerShape(20.dp))
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = if (!isLiveWebPlayerActive) Color(0xFFFF0050) else Color.Transparent,
                modifier = Modifier
                    .clickable { isLiveWebPlayerActive = false }
                    .testTag("preview_mode_visual")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.SmartDisplay,
                        contentDescription = "৯:১৬ ফ্রেম ভিউ",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "৯:১৬ রিল ফ্রেম",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = if (isLiveWebPlayerActive) Color(0xFFFF0050) else Color.Transparent,
                modifier = Modifier
                    .clickable { isLiveWebPlayerActive = true }
                    .testTag("preview_mode_live")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Videocam,
                        contentDescription = "লাইভ প্লেয়ার",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "প্লেয়ার প্রিভিউ (${reelClip.durationSec}s)",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        // 9:16 Aspect Ratio Canvas Container
        Box(
            modifier = Modifier
                .width(260.dp)
                .aspectRatio(9f / 16f)
                .clip(RoundedCornerShape(22.dp))
                .border(2.dp, Brush.verticalGradient(listOf(Color(0xFFFF0050), Color(0xFF8A2BE2))), RoundedCornerShape(22.dp))
                .background(Color.Black)
                .testTag("reel_viewport_container"),
            contentAlignment = Alignment.Center
        ) {
            if (isLiveWebPlayerActive) {
                // Interactive Embedded YouTube Player
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { ctx ->
                        WebView(ctx).apply {
                            settings.javaScriptEnabled = true
                            settings.domStorageEnabled = true
                            settings.mediaPlaybackRequiresUserGesture = false
                            settings.cacheMode = WebSettings.LOAD_DEFAULT
                            webChromeClient = WebChromeClient()
                            webViewClient = WebViewClient()

                            val embedHtml = """
                                <!DOCTYPE html>
                                <html>
                                <head>
                                <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
                                <style>
                                  * { margin:0; padding:0; box-sizing:border-box; background-color: #000; }
                                  body, html { width:100%; height:100%; overflow:hidden; display:flex; justify-content:center; align-items:center; }
                                  iframe { width:100%; height:100%; border:none; }
                                </style>
                                </head>
                                <body>
                                <iframe 
                                  src="https://www.youtube.com/embed/${videoInfo.videoId}?start=${reelClip.startTimeSec}&end=${reelClip.endTimeSec}&autoplay=1&controls=1&playsinline=1&modestbranding=1&rel=0" 
                                  allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" 
                                  allowfullscreen>
                                </iframe>
                                </body>
                                </html>
                            """.trimIndent()
                            loadDataWithBaseURL("https://www.youtube.com", embedHtml, "text/html", "utf-8", null)
                            webViewInstance = this
                        }
                    },
                    update = { view ->
                        val embedHtml = """
                            <!DOCTYPE html>
                            <html>
                            <head>
                            <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
                            <style>
                              * { margin:0; padding:0; box-sizing:border-box; background-color: #000; }
                              body, html { width:100%; height:100%; overflow:hidden; display:flex; justify-content:center; align-items:center; }
                              iframe { width:100%; height:100%; border:none; }
                            </style>
                            </head>
                            <body>
                            <iframe 
                              src="https://www.youtube.com/embed/${videoInfo.videoId}?start=${reelClip.startTimeSec}&end=${reelClip.endTimeSec}&autoplay=1&controls=1&playsinline=1&modestbranding=1&rel=0" 
                              allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" 
                              allowfullscreen>
                            </iframe>
                            </body>
                            </html>
                        """.trimIndent()
                        view.loadDataWithBaseURL("https://www.youtube.com", embedHtml, "text/html", "utf-8", null)
                    }
                )
            } else {
                // Visual 9:16 Mockup with Selected Framing Mode
                when (customization.framingMode) {
                    FramingMode.BLUR_BACKGROUND -> {
                        // Blurred 9:16 backdrop
                        AsyncImage(
                            model = ImageRequest.Builder(context).data(videoInfo.thumbnailUrl).crossfade(true).build(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .blur(24.dp)
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.4f))
                        )
                        // Centered 16:9 main video
                        AsyncImage(
                            model = ImageRequest.Builder(context).data(videoInfo.thumbnailUrl).crossfade(true).build(),
                            contentDescription = "Main video",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(16f / 9f)
                                .clip(RoundedCornerShape(8.dp))
                                .border(1.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                        )
                    }

                    FramingMode.CROP_FILL -> {
                        // 9:16 crop & zoom
                        AsyncImage(
                            model = ImageRequest.Builder(context).data(videoInfo.highResThumbnailUrl).crossfade(true).build(),
                            contentDescription = "Cropped 9:16",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    FramingMode.TOP_BOTTOM_BAR -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceBetween,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Top Bar
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(70.dp)
                                    .background(bannerColor)
                                    .padding(8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = customization.topBannerText,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    textAlign = TextAlign.Center,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            // Center video
                            AsyncImage(
                                model = ImageRequest.Builder(context).data(videoInfo.thumbnailUrl).crossfade(true).build(),
                                contentDescription = "Center video",
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(16f / 9f)
                            )

                            // Bottom Bar
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(70.dp)
                                    .background(Color(0xFF161522))
                                    .padding(8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = customization.subtitleText.ifBlank { reelClip.bengaliCaption.take(50) },
                                    color = Color(0xFFFFD166),
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 11.sp,
                                    textAlign = TextAlign.Center,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }

                    FramingMode.LETTERBOX -> {
                        AsyncImage(
                            model = ImageRequest.Builder(context).data(videoInfo.thumbnailUrl).crossfade(true).build(),
                            contentDescription = "Letterbox video",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(16f / 9f)
                        )
                    }
                }

                // Play overlay button to trigger live player
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .scale(pulseScale)
                        .background(Color.Black.copy(alpha = 0.65f), CircleShape)
                        .border(1.5.dp, Color.White.copy(alpha = 0.7f), CircleShape)
                        .clickable { isLiveWebPlayerActive = true }
                        .testTag("play_reel_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "প্লে করুন",
                        tint = Color.White,
                        modifier = Modifier.size(34.dp)
                    )
                }
            }

            // --- Floating UI Overlays inside Reel (Always Visible) ---

            // Top Header: Hook / Title Pill
            if (customization.framingMode != FramingMode.TOP_BOTTOM_BAR) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 16.dp, start = 12.dp, end = 12.dp)
                        .background(bannerColor, RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = customization.topBannerText.ifBlank { reelClip.hookText },
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Top Right: Viral Score Tag
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 14.dp, end = 8.dp)
                    .background(Color.Black.copy(alpha = 0.7f), RoundedCornerShape(10.dp))
                    .border(1.dp, Color(0xFFFFD166), RoundedCornerShape(10.dp))
                    .padding(horizontal = 6.dp, vertical = 3.dp)
            ) {
                Text(
                    text = "${reelClip.viralScore}% 🔥",
                    color = Color(0xFFFFD166),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Sticker Emoji Overlay (Left or Center)
            if (customization.showSticker) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .offset(x = 12.dp, y = (-40).dp)
                        .size(36.dp)
                        .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                        .border(1.dp, Color.White.copy(alpha = 0.4f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = customization.stickerEmoji,
                        fontSize = 20.sp
                    )
                }
            }

            // Bottom Subtitle & Watermark Pill
            if (customization.framingMode != FramingMode.TOP_BOTTOM_BAR) {
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 14.dp, start = 10.dp, end = 10.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Subtitle Caption Pill
                    if (customization.subtitleText.isNotBlank()) {
                        Box(
                            modifier = Modifier
                                .background(Color.Black.copy(alpha = 0.75f), RoundedCornerShape(8.dp))
                                .border(1.dp, Color(0xFFFFD166).copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = customization.subtitleText,
                                color = Color(0xFFFFD166),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                textAlign = TextAlign.Center,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                    }

                    // Watermark / Channel Tag
                    if (customization.showWatermark && customization.watermarkHandle.isNotBlank()) {
                        Text(
                            text = customization.watermarkHandle,
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            // Bottom-left Timestamp Pill
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(bottom = 8.dp, start = 8.dp)
                    .background(Color.Black.copy(alpha = 0.75f), RoundedCornerShape(6.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "${reelClip.startTimeSec}s - ${reelClip.endTimeSec}s",
                    color = Color.White,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
