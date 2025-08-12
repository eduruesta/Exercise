package org.dev.exercises.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.UIKitInteropProperties
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFoundation.*
import platform.AVKit.AVPlayerViewController
import platform.Foundation.NSURL

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun VideoPlayer(
    videoUrl: String,
    modifier: Modifier
) {
    var hasError by remember { mutableStateOf(false) }
    
    val avPlayer = remember {
        val url = NSURL.URLWithString(videoUrl)
        if (url != null) {
            AVPlayer(uRL = url)
        } else {
            hasError = true
            null
        }
    }
    
    DisposableEffect(avPlayer) {
        onDispose {
            avPlayer?.pause()
        }
    }
    
    Box(modifier = modifier) {
        if (hasError || avPlayer == null) {
            // Show error state
            Card(
                modifier = Modifier.fillMaxSize(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "⚠️",
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Video not available",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }
        } else {
            // Show video player
            UIKitView(
                factory = {
                    val playerViewController = AVPlayerViewController()
                    playerViewController.player = avPlayer
                    playerViewController.showsPlaybackControls = true
                    playerViewController.view
                },
                modifier = Modifier.fillMaxSize(),
                update = { },
                onRelease = { },
                properties = UIKitInteropProperties(isInteractive = true, isNativeAccessibilityEnabled = true)
            )
        }
    }
}