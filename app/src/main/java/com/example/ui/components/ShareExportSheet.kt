package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Launch
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ReelClip
import com.example.data.model.YouTubeVideoInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShareExportSheet(
    videoInfo: YouTubeVideoInfo,
    reelClip: ReelClip,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onSaveToDb: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val timestampedUrl = "https://youtu.be/${videoInfo.videoId}?t=${reelClip.startTimeSec}s"
    val fullShareText = reelClip.fullShareText(timestampedUrl)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color(0xFF161522),
        modifier = modifier.testTag("share_export_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Text(
                text = "রিল এক্সপোর্ট ও শেয়ার হাব 🚀",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "ইউটিউব শর্টস বা ইনস্টাগ্রাম রিল হিসেবে পোস্ট করার জন্য রেডি",
                fontSize = 12.sp,
                color = Color(0xFFA1A1AA)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Caption Preview Card
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF211F34)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = reelClip.hookText,
                        color = Color(0xFFFF0050),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = reelClip.bengaliCaption,
                        color = Color.White,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = reelClip.hashtags.joinToString(" "),
                        color = Color(0xFF00F2FE),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 1. Copy All (Caption + Tags + Link) Button
            Button(
                onClick = {
                    copyToClipboard(context, "Reel Content", fullShareText)
                    Toast.makeText(context, "ক্যাপশন ও হ্যাশট্যাগ কপি করা হয়েছে! 📋", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("btn_copy_caption"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF0050))
            ) {
                Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("ক্যাপশন ও হ্যাশট্যাগ কপি করুন", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 2. Share to Social Media (Instagram, WhatsApp, TikTok)
            Button(
                onClick = {
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_SUBJECT, reelClip.hookText)
                        putExtra(Intent.EXTRA_TEXT, fullShareText)
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "শেয়ার করুন (Share Reel)"))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("btn_share_intent"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7928CA))
            ) {
                Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("সোশ্যাল মিডিয়ায় শেয়ার করুন", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 3. Open in YouTube App / Shorts Creator
            OutlinedButton(
                onClick = {
                    try {
                        val ytIntent = Intent(Intent.ACTION_VIEW, Uri.parse(timestampedUrl))
                        context.startActivity(ytIntent)
                    } catch (e: Exception) {
                        Toast.makeText(context, "ইউটিউব অ্যাপ ওপেন করা যায়নি", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("btn_open_youtube"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
            ) {
                Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = null, tint = Color(0xFFFFD166), modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("ইউটিউবে ক্লিপ ওপেন করুন", color = Color(0xFFFFD166), fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 4. Save to Project Library
            OutlinedButton(
                onClick = {
                    onSaveToDb()
                    onDismiss()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("btn_save_project"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF06D6A0))
            ) {
                Icon(Icons.Default.BookmarkAdd, contentDescription = null, tint = Color(0xFF06D6A0), modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("সংরক্ষিত রিল এ সেভ করুন", color = Color(0xFF06D6A0), fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

private fun copyToClipboard(context: Context, label: String, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText(label, text)
    clipboard.setPrimaryClip(clip)
}
