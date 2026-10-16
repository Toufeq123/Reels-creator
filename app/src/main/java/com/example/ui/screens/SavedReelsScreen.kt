package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.data.local.ReelProjectEntity
import com.example.ui.viewmodel.ReelViewModel
import com.example.ui.viewmodel.StudioTab
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SavedReelsScreen(
    viewModel: ReelViewModel,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val savedReels by viewModel.savedProjects.collectAsState()

    if (savedReels.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color(0xFF0D0C13))
                .padding(contentPadding)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(Color(0xFF211F34), RoundedCornerShape(24.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.BookmarkBorder,
                        contentDescription = null,
                        tint = Color(0xFFFF0050),
                        modifier = Modifier.size(44.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "কোনো রিল সেভ করা নেই",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "ইউটিউব লিংক পেস্ট করে রিল তৈরি করুন এবং 'সেভ করুন' বাটনে ট্যাপ করুন।",
                    fontSize = 13.sp,
                    color = Color(0xFFA1A1AA),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { viewModel.setTab(StudioTab.STUDIO) },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF0050)),
                    modifier = Modifier.testTag("btn_create_first_reel")
                ) {
                    Icon(Icons.Default.Movie, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("রিল তৈরি করুন", fontWeight = FontWeight.Bold)
                }
            }
        }
    } else {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(Color(0xFF0D0C13))
                .padding(contentPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "সংরক্ষিত রিল প্রজেক্ট (${savedReels.size}টি)",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "যেকোনো সময় এডিট ও সোশ্যাল মিডিয়ায় শেয়ার করুন",
                            fontSize = 11.sp,
                            color = Color(0xFFA1A1AA)
                        )
                    }
                }
            }

            items(savedReels, key = { it.id }) { item ->
                SavedReelCard(
                    project = item,
                    onOpenInStudio = {
                        viewModel.loadSampleVideo(item.youtubeUrl)
                        viewModel.setTab(StudioTab.STUDIO)
                    },
                    onDelete = { viewModel.deleteSavedReel(item.id) },
                    onCopyCaption = {
                        val text = "${item.hookText}\n\n${item.captionText}\n\n${item.hashtags}\n🔗 ${item.youtubeUrl}?t=${item.startTimeSec}s"
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        clipboard.setPrimaryClip(ClipData.newPlainText("Reel Content", text))
                        Toast.makeText(context, "ক্যাপশন ও হ্যাশট্যাগ কপি করা হয়েছে! 📋", Toast.LENGTH_SHORT).show()
                    },
                    onShare = {
                        val text = "${item.hookText}\n\n${item.captionText}\n\n${item.hashtags}\n🔗 ${item.youtubeUrl}?t=${item.startTimeSec}s"
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_SUBJECT, item.hookText)
                            putExtra(Intent.EXTRA_TEXT, text)
                        }
                        context.startActivity(Intent.createChooser(intent, "শেয়ার করুন (Share Reel)"))
                    }
                )
            }
        }
    }
}

@Composable
fun SavedReelCard(
    project: ReelProjectEntity,
    onOpenInStudio: () -> Unit,
    onDelete: () -> Unit,
    onCopyCaption: () -> Unit,
    onShare: () -> Unit
) {
    val durationSec = project.endTimeSec - project.startTimeSec
    val dateFormat = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
    val dateString = dateFormat.format(Date(project.createdAt))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onOpenInStudio() }
            .testTag("saved_reel_card_${project.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF161522)),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2E2B45))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Thumbnail
                AsyncImage(
                    model = project.thumbnailUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(width = 80.dp, height = 55.dp)
                        .clip(RoundedCornerShape(8.dp))
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = project.hookText,
                        color = Color(0xFFFF0050),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = project.videoTitle,
                        color = Color.White,
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color(0xFF211F34)
                        ) {
                            Text(
                                text = "${durationSec}s রিল",
                                color = Color(0xFFFFD166),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }

                        Text(
                            text = dateString,
                            color = Color(0xFFA1A1AA),
                            fontSize = 10.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Action Buttons Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    IconButton(
                        onClick = onCopyCaption,
                        modifier = Modifier.size(36.dp).testTag("saved_copy_btn_${project.id}")
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = "কপি", tint = Color(0xFF00F2FE), modifier = Modifier.size(18.dp))
                    }

                    IconButton(
                        onClick = onShare,
                        modifier = Modifier.size(36.dp).testTag("saved_share_btn_${project.id}")
                    ) {
                        Icon(Icons.Default.Share, contentDescription = "শেয়ার", tint = Color(0xFFA855F7), modifier = Modifier.size(18.dp))
                    }

                    IconButton(
                        onClick = onOpenInStudio,
                        modifier = Modifier.size(36.dp).testTag("saved_edit_btn_${project.id}")
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "এডিট", tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(36.dp).testTag("saved_delete_btn_${project.id}")
                ) {
                    Icon(Icons.Default.DeleteOutline, contentDescription = "মুছুন", tint = Color(0xFFFF5252), modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}
