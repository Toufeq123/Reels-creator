package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Crop
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FramingMode
import com.example.data.model.ReelCustomization

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ReelStyleCustomizer(
    customization: ReelCustomization,
    onFramingModeChange: (FramingMode) -> Unit,
    onBannerColorChange: (String) -> Unit,
    onTopBannerTextChange: (String) -> Unit,
    onSubtitleTextChange: (String) -> Unit,
    onWatermarkChange: (String) -> Unit,
    onToggleWatermark: (Boolean) -> Unit,
    onStickerEmojiChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val bannerColors = listOf(
        "#FF0050" to "Neon Red",
        "#8A2BE2" to "Cyber Purple",
        "#FFD166" to "Vibrant Gold",
        "#00F2FE" to "Electric Cyan",
        "#06D6A0" to "Emerald",
        "#E63946" to "Crimson",
        "#1E1B2E" to "Deep Slate"
    )

    val emojiStickers = listOf("🔥", "😱", "💡", "🤯", "⚡", "🎯", "✨", "🎬", "🚀", "👏")

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("style_customizer_card"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF161522))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Palette,
                    contentDescription = null,
                    tint = Color(0xFFA855F7),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "রিল ফ্রেম ও স্টাইল কাস্টমাইজেশন",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 1. Framing Mode Selector
            Text(
                text = "ভিডিও ফরম্যাট (Framing Mode):",
                fontSize = 12.sp,
                color = Color(0xFFA1A1AA),
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(6.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                FramingMode.entries.forEach { mode ->
                    val isSelected = customization.framingMode == mode
                    Box(
                        modifier = Modifier
                            .background(
                                if (isSelected) Color(0xFFA855F7) else Color(0xFF211F34),
                                RoundedCornerShape(10.dp)
                            )
                            .border(1.dp, if (isSelected) Color(0xFFA855F7) else Color(0xFF2E2B45), RoundedCornerShape(10.dp))
                            .clickable { onFramingModeChange(mode) }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                            .testTag("framing_mode_${mode.name}")
                    ) {
                        Text(
                            text = mode.labelBn,
                            color = if (isSelected) Color.White else Color(0xFFA1A1AA),
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 2. Banner Color Palette
            Text(
                text = "হুক ব্যানার কালার:",
                fontSize = 12.sp,
                color = Color(0xFFA1A1AA),
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                bannerColors.forEach { (hex, _) ->
                    val color = Color(android.graphics.Color.parseColor(hex))
                    val isSelected = customization.bannerColorHex.equals(hex, ignoreCase = true)
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .background(color, CircleShape)
                            .border(
                                width = if (isSelected) 3.dp else 1.dp,
                                color = if (isSelected) Color.White else Color.Transparent,
                                shape = CircleShape
                            )
                            .clickable { onBannerColorChange(hex) }
                            .testTag("color_pill_$hex")
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 3. Emoji Sticker Selector
            Text(
                text = "ভাইরাল স্টিকার ইমোজি:",
                fontSize = 12.sp,
                color = Color(0xFFA1A1AA),
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                emojiStickers.take(7).forEach { emoji ->
                    val isSelected = customization.stickerEmoji == emoji
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .background(
                                if (isSelected) Color(0xFFFF0050).copy(alpha = 0.3f) else Color(0xFF211F34),
                                RoundedCornerShape(8.dp)
                            )
                            .border(1.dp, if (isSelected) Color(0xFFFF0050) else Color(0xFF2E2B45), RoundedCornerShape(8.dp))
                            .clickable { onStickerEmojiChange(emoji) }
                            .testTag("sticker_$emoji"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = emoji, fontSize = 18.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 4. Hook Text Field
            OutlinedTextField(
                value = customization.topBannerText,
                onValueChange = onTopBannerTextChange,
                label = { Text("হুক হেডলাইন (Hook Banner)", fontSize = 12.sp) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_hook_text"),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFFF0050),
                    unfocusedBorderColor = Color(0xFF2E2B45),
                    focusedContainerColor = Color(0xFF211F34),
                    unfocusedContainerColor = Color(0xFF211F34),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // 5. Subtitle Caption Field
            OutlinedTextField(
                value = customization.subtitleText,
                onValueChange = onSubtitleTextChange,
                label = { Text("সাবটাইটেল / ক্যাপশন টেক্সট", fontSize = 12.sp) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_subtitle_text"),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFFFD166),
                    unfocusedBorderColor = Color(0xFF2E2B45),
                    focusedContainerColor = Color(0xFF211F34),
                    unfocusedContainerColor = Color(0xFF211F34),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // 6. Watermark handle row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedTextField(
                    value = customization.watermarkHandle,
                    onValueChange = onWatermarkChange,
                    label = { Text("ওয়াটারমার্ক (@handle)", fontSize = 12.sp) },
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("input_watermark"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFA855F7),
                        unfocusedBorderColor = Color(0xFF2E2B45),
                        focusedContainerColor = Color(0xFF211F34),
                        unfocusedContainerColor = Color(0xFF211F34),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.width(10.dp))

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "দেখান", fontSize = 10.sp, color = Color(0xFFA1A1AA))
                    Switch(
                        checked = customization.showWatermark,
                        onCheckedChange = onToggleWatermark,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Color(0xFFFF0050)
                        ),
                        modifier = Modifier.testTag("switch_watermark")
                    )
                }
            }
        }
    }
}
