package com.example

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.SmartDisplay
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.SmartDisplay
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.screens.MainStudioScreen
import com.example.ui.screens.SavedReelsScreen
import com.example.ui.screens.TemplatesScreen
import com.example.ui.theme.ReelSnapTheme
import com.example.ui.viewmodel.ReelViewModel
import com.example.ui.viewmodel.StudioTab

class MainActivity : ComponentActivity() {

    private val viewModel: ReelViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Handle shared URL from external apps (e.g., YouTube app share sheet)
        handleIncomingIntent(intent)

        setContent {
            ReelSnapTheme {
                val activeTab by viewModel.activeTab.collectAsState()

                // Handle back button to return to Studio tab
                BackHandler(enabled = activeTab != StudioTab.STUDIO) {
                    viewModel.setTab(StudioTab.STUDIO)
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color(0xFF0D0C13),
                    bottomBar = {
                        NavigationBar(
                            containerColor = Color(0xFF161522),
                            contentColor = Color.White,
                            tonalElevation = 8.dp,
                            modifier = Modifier
                                .windowInsetsPadding(WindowInsets.navigationBars)
                                .testTag("main_bottom_nav")
                        ) {
                            // Tab 1: Studio
                            NavigationBarItem(
                                selected = activeTab == StudioTab.STUDIO,
                                onClick = { viewModel.setTab(StudioTab.STUDIO) },
                                icon = {
                                    Icon(
                                        imageVector = if (activeTab == StudioTab.STUDIO) Icons.Filled.SmartDisplay else Icons.Outlined.SmartDisplay,
                                        contentDescription = "রিল মেকার"
                                    )
                                },
                                label = {
                                    Text(
                                        text = StudioTab.STUDIO.labelBn,
                                        fontSize = 11.sp,
                                        fontWeight = if (activeTab == StudioTab.STUDIO) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = Color.White,
                                    selectedTextColor = Color(0xFFFF0050),
                                    indicatorColor = Color(0xFFFF0050),
                                    unselectedIconColor = Color(0xFFA1A1AA),
                                    unselectedTextColor = Color(0xFFA1A1AA)
                                ),
                                modifier = Modifier.testTag("nav_item_studio")
                            )

                            // Tab 2: Saved Reels
                            NavigationBarItem(
                                selected = activeTab == StudioTab.SAVED,
                                onClick = { viewModel.setTab(StudioTab.SAVED) },
                                icon = {
                                    Icon(
                                        imageVector = if (activeTab == StudioTab.SAVED) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                                        contentDescription = "সংরক্ষিত রিল"
                                    )
                                },
                                label = {
                                    Text(
                                        text = StudioTab.SAVED.labelBn,
                                        fontSize = 11.sp,
                                        fontWeight = if (activeTab == StudioTab.SAVED) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = Color.White,
                                    selectedTextColor = Color(0xFFFF0050),
                                    indicatorColor = Color(0xFFFF0050),
                                    unselectedIconColor = Color(0xFFA1A1AA),
                                    unselectedTextColor = Color(0xFFA1A1AA)
                                ),
                                modifier = Modifier.testTag("nav_item_saved")
                            )

                            // Tab 3: Templates
                            NavigationBarItem(
                                selected = activeTab == StudioTab.TEMPLATES,
                                onClick = { viewModel.setTab(StudioTab.TEMPLATES) },
                                icon = {
                                    Icon(
                                        imageVector = if (activeTab == StudioTab.TEMPLATES) Icons.Filled.AutoAwesome else Icons.Outlined.AutoAwesome,
                                        contentDescription = "টেমপ্লেট"
                                    )
                                },
                                label = {
                                    Text(
                                        text = StudioTab.TEMPLATES.labelBn,
                                        fontSize = 11.sp,
                                        fontWeight = if (activeTab == StudioTab.TEMPLATES) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = Color.White,
                                    selectedTextColor = Color(0xFFFF0050),
                                    indicatorColor = Color(0xFFFF0050),
                                    unselectedIconColor = Color(0xFFA1A1AA),
                                    unselectedTextColor = Color(0xFFA1A1AA)
                                ),
                                modifier = Modifier.testTag("nav_item_templates")
                            )
                        }
                    }
                ) { innerPadding ->
                    Crossfade(targetState = activeTab, label = "tab_transition") { tab ->
                        when (tab) {
                            StudioTab.STUDIO -> MainStudioScreen(viewModel = viewModel, contentPadding = innerPadding)
                            StudioTab.SAVED -> SavedReelsScreen(viewModel = viewModel, contentPadding = innerPadding)
                            StudioTab.TEMPLATES -> TemplatesScreen(viewModel = viewModel, contentPadding = innerPadding)
                        }
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIncomingIntent(intent)
    }

    private fun handleIncomingIntent(intent: Intent?) {
        if (intent?.action == Intent.ACTION_SEND && intent.type == "text/plain") {
            val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
            if (!sharedText.isNullOrBlank()) {
                viewModel.onUrlChanged(sharedText)
                viewModel.processYouTubeUrl(sharedText)
                viewModel.setTab(StudioTab.STUDIO)
            }
        }
    }
}
