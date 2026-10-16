package com.example.ui.screens

import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.model.ReelClip
import com.example.ui.components.ReelStyleCustomizer
import com.example.ui.components.ReelViewportPlayer
import com.example.ui.components.ShareExportSheet
import com.example.ui.components.TimeTrimmerControls
import com.example.ui.viewmodel.ReelViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainStudioScreen(
    viewModel: ReelViewModel,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val urlInput by viewModel.urlInput.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val videoInfo by viewModel.videoInfo.collectAsState()
    val reelClips by viewModel.reelClips.collectAsState()
    val selectedReel by viewModel.selectedReel.collectAsState()
    val customization by viewModel.customization.collectAsState()
    val saveMessage by viewModel.saveSuccessMessage.collectAsState()

    var showShareSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    if (saveMessage != null) {
        Toast.makeText(context, saveMessage, Toast.LENGTH_SHORT).show()
        viewModel.clearSaveSuccessMessage()
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0D0C13))
            .padding(contentPadding),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // App Hero Header
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("hero_header_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF161522))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFFFF0050).copy(alpha = 0.25f), Color(0xFF7928CA).copy(alpha = 0.25f))
                            )
                        )
                        .padding(16.dp)
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .background(Brush.linearGradient(listOf(Color(0xFFFF0050), Color(0xFF7928CA))), RoundedCornerShape(10.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PlayCircle,
                                        contentDescription = "Logo",
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "ReelSnap স্টুডিও",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "ইউটিউব URL থেকে ভাইরাল রিল মেকার",
                                        fontSize = 11.sp,
                                        color = Color(0xFFA1A1AA)
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFFFFD166).copy(alpha = 0.2f),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFD166))
                            ) {
                                Text(
                                    text = "AI 9:16",
                                    color = Color(0xFFFFD166),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
        }

        // YouTube URL Input Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("url_input_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF161522))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "ইউটিউব ভিডিওর লিংক দিন:",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = urlInput,
                        onValueChange = { viewModel.onUrlChanged(it) },
                        placeholder = { Text("https://www.youtube.com/watch?v=...", fontSize = 12.sp, color = Color(0xFF71717A)) },
                        singleLine = true,
                        leadingIcon = {
                            Icon(Icons.Default.Link, contentDescription = null, tint = Color(0xFFFF0050))
                        },
                        trailingIcon = {
                            Row {
                                if (urlInput.isNotBlank()) {
                                    IconButton(
                                        onClick = { viewModel.clearUrl() },
                                        modifier = Modifier.testTag("btn_clear_url")
                                    ) {
                                        Icon(Icons.Default.Clear, contentDescription = "মুছুন", tint = Color(0xFFA1A1AA))
                                    }
                                }
                                IconButton(
                                    onClick = {
                                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                        if (clipboard.hasPrimaryClip() && clipboard.primaryClipDescription?.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN) == true) {
                                            val text = clipboard.primaryClip?.getItemAt(0)?.text?.toString() ?: ""
                                            if (text.isNotBlank()) {
                                                viewModel.onUrlChanged(text)
                                                Toast.makeText(context, "লিংক পেস্ট করা হয়েছে!", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    },
                                    modifier = Modifier.testTag("btn_paste_url")
                                ) {
                                    Icon(Icons.Default.ContentPaste, contentDescription = "পেস্ট", tint = Color(0xFF00F2FE))
                                }
                            }
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFFF0050),
                            unfocusedBorderColor = Color(0xFF2E2B45),
                            focusedContainerColor = Color(0xFF211F34),
                            unfocusedContainerColor = Color(0xFF211F34),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_youtube_url")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Sample Quick Links
                    Text(
                        text = "অথবা নিচের স্যাম্পল ভিডিও দিয়ে চেষ্টা করুন:",
                        fontSize = 11.sp,
                        color = Color(0xFFA1A1AA)
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(viewModel.sampleVideos) { sample ->
                            Box(
                                modifier = Modifier
                                    .background(Color(0xFF211F34), RoundedCornerShape(10.dp))
                                    .border(1.dp, Color(0xFF2E2B45), RoundedCornerShape(10.dp))
                                    .clickable { viewModel.loadSampleVideo(sample.url) }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                                    .testTag("sample_chip_${sample.category}")
                            ) {
                                Text(
                                    text = "${sample.category} • ${sample.titleBn}",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Generate Button
                    Button(
                        onClick = { viewModel.processYouTubeUrl() },
                        enabled = !isLoading && urlInput.isNotBlank(),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF0050)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("btn_generate_reels")
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("ভিডিও বিশ্লেষণ ও রিল তৈরি হচ্ছে...", fontWeight = FontWeight.Bold, color = Color.White)
                        } else {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("রিল তৈরি করুন (Generate Reels)", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }

                    // Error Message Display
                    if (errorMessage != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = errorMessage ?: "",
                            color = Color(0xFFFF5252),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
        }

        // Active Video Details Header Card
        videoInfo?.let { video ->
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("video_details_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF161522))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(context).data(video.thumbnailUrl).crossfade(true).build(),
                            contentDescription = "Thumbnail",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(width = 80.dp, height = 55.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .border(1.dp, Color(0xFF2E2B45), RoundedCornerShape(8.dp))
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = video.title,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = "চ্যানেল: ${video.authorName}",
                                color = Color(0xFFA1A1AA),
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
            }
        }

        // Generated Reel Clips Carousel / Selector
        if (reelClips.isNotEmpty()) {
            item {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "চিহ্নিত ভাইরাল মুহূর্ত (${reelClips.size}টি রিল)",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "যেকোনো একটি নির্বাচন করুন",
                            fontSize = 11.sp,
                            color = Color(0xFFA1A1AA)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(reelClips) { clip ->
                            val isSelected = selectedReel?.id == clip.id
                            ReelClipSelectorCard(
                                clip = clip,
                                isSelected = isSelected,
                                onClick = { viewModel.selectReel(clip) }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // Main 9:16 Reel Player and Studio Viewport
        if (videoInfo != null && selectedReel != null) {
            item {
                ReelViewportPlayer(
                    videoInfo = videoInfo!!,
                    reelClip = selectedReel!!,
                    customization = customization
                )

                Spacer(modifier = Modifier.height(14.dp))
            }

            // Time Trimmer Controls
            item {
                TimeTrimmerControls(
                    selectedClip = selectedReel!!,
                    maxVideoDurationSec = videoInfo?.approxDurationSec ?: 300,
                    onStartTimeChange = { viewModel.updateStartTime(it) },
                    onEndTimeChange = { viewModel.updateEndTime(it) },
                    onQuickDurationPreset = { viewModel.setQuickDuration(it) }
                )

                Spacer(modifier = Modifier.height(14.dp))
            }

            // Style and Framing Customizer
            item {
                ReelStyleCustomizer(
                    customization = customization,
                    onFramingModeChange = { viewModel.updateFramingMode(it) },
                    onBannerColorChange = { viewModel.updateBannerColor(it) },
                    onTopBannerTextChange = { viewModel.updateTopBannerText(it) },
                    onSubtitleTextChange = { viewModel.updateSubtitleText(it) },
                    onWatermarkChange = { viewModel.updateWatermark(it) },
                    onToggleWatermark = { viewModel.toggleWatermark(it) },
                    onStickerEmojiChange = { viewModel.setStickerEmoji(it) }
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Bottom Sticky Save and Share Action Buttons
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { viewModel.saveCurrentReel() },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .testTag("btn_save_to_library"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF211F34))
                    ) {
                        Text("সেভ করুন 📌", color = Color.White, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { showShareSheet = true },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .testTag("btn_open_share_hub"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF0050))
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("এক্সপোর্ট ও শেয়ার 🚀", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    if (showShareSheet && videoInfo != null && selectedReel != null) {
        ShareExportSheet(
            videoInfo = videoInfo!!,
            reelClip = selectedReel!!,
            sheetState = sheetState,
            onDismiss = { showShareSheet = false },
            onSaveToDb = { viewModel.saveCurrentReel() }
        )
    }
}

@Composable
fun ReelClipSelectorCard(
    clip: ReelClip,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(180.dp)
            .clickable { onClick() }
            .testTag("clip_card_${clip.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFF261D36) else Color(0xFF161522)
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) Color(0xFFFF0050) else Color(0xFF2E2B45)
        )
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFFFF0050).copy(alpha = 0.2f)
                ) {
                    Text(
                        text = clip.categoryTag,
                        color = Color(0xFFFF0050),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Text(
                    text = "${clip.viralScore}% 🔥",
                    color = Color(0xFFFFD166),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = clip.title,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = clip.formattedTimeRange(),
                color = Color(0xFFA1A1AA),
                fontSize = 10.sp
            )
        }
    }
}
