package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FramingMode
import com.example.ui.viewmodel.ReelViewModel

data class ViralTemplate(
    val id: String,
    val nameBn: String,
    val nameEn: String,
    val description: String,
    val defaultHook: String,
    val framingMode: FramingMode,
    val bannerColorHex: String,
    val emoji: String,
    val badge: String
)

@Composable
fun TemplatesScreen(
    viewModel: ReelViewModel,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    val templates = listOf(
        ViralTemplate(
            id = "t1",
            nameBn = "পডকাস্ট ভাইরাল হুক (Podcast Punch)",
            nameEn = "Podcast Viral Punch",
            description = "পডকাস্ট ও ইন্টারভিউ ক্লিপের জন্য সেরা। ব্লার ব্যাকগ্রাউন্ড ও আকর্ষণীয় টেক্সট হুক।",
            defaultHook = "এই একটি ভুল সবাই করে! 💡",
            framingMode = FramingMode.BLUR_BACKGROUND,
            bannerColorHex = "#FF0050",
            emoji = "💡",
            badge = "🔥 ট্রেন্ডিং #১"
        ),
        ViralTemplate(
            id = "t2",
            nameBn = "গেমিং ও ক্লাইম্যাক্স হাইপ (Gaming Peak)",
            nameEn = "Gaming Peak & Climax",
            description = "ফুলস্ক্রিন ৯:১৬ ক্রপ জুম এবং নিয়ন ভায়োলেট স্টাইল। গেমপ্লে ও অ্যাকশন ভিডিওর জন্য উপযুক্ত।",
            defaultHook = "অবিশ্বাস্য ক্লাইম্যাক্স মুহূর্ত! 🤯",
            framingMode = FramingMode.CROP_FILL,
            bannerColorHex = "#8A2BE2",
            emoji = "🤯",
            badge = "⚡ হাই এনার্জি"
        ),
        ViralTemplate(
            id = "t3",
            nameBn = "হেডার ও ফুটার ব্যানার (Split Header)",
            nameEn = "Top & Bottom Banner",
            description = "উপরে বড় শিরোনাম এবং নিচে ক্যাপশন বার। ফেসবুক ও টিকটকে সবচেয়ে বেশি ভিউ পায়।",
            defaultHook = "WAIT TILL THE END 😱",
            framingMode = FramingMode.TOP_BOTTOM_BAR,
            bannerColorHex = "#FF0050",
            emoji = "😱",
            badge = "📈 সর্বোচ্চ ভিউ"
        ),
        ViralTemplate(
            id = "t4",
            nameBn = "টেক ও গ্যাজেট ইনসাইট (Tech Quickie)",
            nameEn = "Tech & Gadget Insight",
            description = "পরিচ্ছন্ন সাইবার সিয়ান থিম। রিভিউ ও মোবাইল ট্রিকস দেখানোর জন্য পারফেক্ট।",
            defaultHook = "এই গোপন ট্রিকটি কেউ জানেনা! 🔥",
            framingMode = FramingMode.BLUR_BACKGROUND,
            bannerColorHex = "#00F2FE",
            emoji = "🔥",
            badge = "📱 টেক স্পেশাল"
        ),
        ViralTemplate(
            id = "t5",
            nameBn = "মোটিভেশনাল কোট ও জ্ঞান (Wisdom)",
            nameEn = "Motivation & Wisdom",
            description = "গোল্ডেন এক্সেন্ট ও সাবটাইটেল হাইলাইট। জীবন পরিবর্তনকারী বক্তব্যের জন্য আদর্শ।",
            defaultHook = "ভবিষ্যত বদলে দেওয়ার ৩টি নিয়ম 🎯",
            framingMode = FramingMode.BLUR_BACKGROUND,
            bannerColorHex = "#FFD166",
            emoji = "🎯",
            badge = "✨ অনুপ্রেরণা"
        )
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0D0C13))
            .padding(contentPadding),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Column {
                Text(
                    text = "ভাইরাল রিল টেমপ্লেট সংগ্রহ 🎨",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "এক ট্যাপে পেশাদার ক্রিয়েটর স্টাইল ও ফরম্যাটিং অ্যাপ্লাই করুন",
                    fontSize = 11.sp,
                    color = Color(0xFFA1A1AA)
                )
            }
        }

        items(templates) { template ->
            TemplateCard(
                template = template,
                onApply = {
                    viewModel.applyTemplate(
                        templateTitle = template.nameBn,
                        hook = template.defaultHook,
                        framingMode = template.framingMode,
                        bannerColor = template.bannerColorHex,
                        emoji = template.emoji
                    )
                }
            )
        }
    }
}

@Composable
fun TemplateCard(
    template: ViralTemplate,
    onApply: () -> Unit
) {
    val bannerColor = try {
        Color(android.graphics.Color.parseColor(template.bannerColorHex))
    } catch (e: Exception) {
        Color(0xFFFF0050)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("template_card_${template.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF161522)),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2E2B45))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = bannerColor.copy(alpha = 0.2f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, bannerColor)
                ) {
                    Text(
                        text = template.badge,
                        color = bannerColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                Text(
                    text = "${template.emoji} ${template.framingMode.labelBn}",
                    color = Color(0xFFA1A1AA),
                    fontSize = 11.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = template.nameBn,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = template.description,
                fontSize = 12.sp,
                color = Color(0xFFA1A1AA)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Sample Hook Pill
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(bannerColor.copy(alpha = 0.15f), RoundedCornerShape(10.dp))
                    .border(1.dp, bannerColor.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "হুক উদাহরণ: ", fontSize = 11.sp, color = Color(0xFFA1A1AA))
                    Text(text = template.defaultHook, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = onApply,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .testTag("btn_apply_template_${template.id}"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = bannerColor)
            ) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("এই টেমপ্লেট ব্যবহার করুন", fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}
