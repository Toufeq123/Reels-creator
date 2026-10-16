package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ReelClip

@Composable
fun TimeTrimmerControls(
    selectedClip: ReelClip,
    maxVideoDurationSec: Int = 300,
    onStartTimeChange: (Int) -> Unit,
    onEndTimeChange: (Int) -> Unit,
    onQuickDurationPreset: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("time_trimmer_card"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF161522))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header: Duration and label
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.ContentCut,
                        contentDescription = "ট্রিমার",
                        tint = Color(0xFFFF0050),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "রিল ট্রিমার ও সময়সীমা",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFFF0050).copy(alpha = 0.2f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFF0050))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = null,
                            tint = Color(0xFFFF0050),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${selectedClip.durationSec} সেকেন্ড",
                            color = Color(0xFFFF0050),
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Start & End Timestamps stepper boxes
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Start Time Box
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .background(Color(0xFF211F34), RoundedCornerShape(12.dp))
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "শুরুর সময় (Start)",
                        fontSize = 11.sp,
                        color = Color(0xFFA1A1AA)
                    )
                    Text(
                        text = formatSeconds(selectedClip.startTimeSec),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        IconButton(
                            onClick = { onStartTimeChange(selectedClip.startTimeSec - 1) },
                            modifier = Modifier.size(32.dp).testTag("btn_start_minus")
                        ) {
                            Icon(Icons.Default.Remove, contentDescription = "-1s", tint = Color.White, modifier = Modifier.size(16.dp))
                        }
                        IconButton(
                            onClick = { onStartTimeChange(selectedClip.startTimeSec + 1) },
                            modifier = Modifier.size(32.dp).testTag("btn_start_plus")
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "+1s", tint = Color.White, modifier = Modifier.size(16.dp))
                        }
                    }
                }

                // End Time Box
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .background(Color(0xFF211F34), RoundedCornerShape(12.dp))
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "শেষের সময় (End)",
                        fontSize = 11.sp,
                        color = Color(0xFFA1A1AA)
                    )
                    Text(
                        text = formatSeconds(selectedClip.endTimeSec),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        IconButton(
                            onClick = { onEndTimeChange(selectedClip.endTimeSec - 1) },
                            modifier = Modifier.size(32.dp).testTag("btn_end_minus")
                        ) {
                            Icon(Icons.Default.Remove, contentDescription = "-1s", tint = Color.White, modifier = Modifier.size(16.dp))
                        }
                        IconButton(
                            onClick = { onEndTimeChange(selectedClip.endTimeSec + 1) },
                            modifier = Modifier.size(32.dp).testTag("btn_end_plus")
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "+1s", tint = Color.White, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Start Slider
            Column {
                Text(
                    text = "ভিডিও ট্রিম স্লাইডার (${selectedClip.startTimeSec}s - ${selectedClip.endTimeSec}s)",
                    fontSize = 12.sp,
                    color = Color(0xFFA1A1AA)
                )
                Slider(
                    value = selectedClip.startTimeSec.toFloat(),
                    onValueChange = { onStartTimeChange(it.toInt()) },
                    valueRange = 0f..(maxVideoDurationSec - 15).toFloat(),
                    colors = SliderDefaults.colors(
                        thumbColor = Color(0xFFFF0050),
                        activeTrackColor = Color(0xFFFF0050),
                        inactiveTrackColor = Color(0xFF2E2B45)
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("start_slider")
                )
            }

            // Quick Preset Chips (15s, 30s, 45s, 60s)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "দ্রুত দৈর্ঘ্য:",
                    fontSize = 12.sp,
                    color = Color(0xFFA1A1AA)
                )
                listOf(15, 30, 45, 60).forEach { sec ->
                    val isSelected = selectedClip.durationSec == sec
                    Box(
                        modifier = Modifier
                            .background(
                                if (isSelected) Color(0xFFFF0050) else Color(0xFF211F34),
                                RoundedCornerShape(10.dp)
                            )
                            .border(1.dp, if (isSelected) Color(0xFFFF0050) else Color(0xFF2E2B45), RoundedCornerShape(10.dp))
                            .clickable { onQuickDurationPreset(sec) }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                            .testTag("preset_${sec}s")
                    ) {
                        Text(
                            text = "${sec}s",
                            color = if (isSelected) Color.White else Color(0xFFA1A1AA),
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

private fun formatSeconds(seconds: Int): String {
    val m = seconds / 60
    val s = seconds % 60
    return "%02d:%02d".format(m, s)
}
