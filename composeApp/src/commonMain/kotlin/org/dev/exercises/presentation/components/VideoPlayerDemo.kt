package org.dev.exercises.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Demo component to test video player functionality
 * You can use this in development to test video playback with sample URLs
 */
@Composable
fun VideoPlayerDemo() {
    var isSubscribed by remember { mutableStateOf(false) }
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Video Player Demo",
                    style = MaterialTheme.typography.headlineMedium
                )
                
                Switch(
                    checked = isSubscribed,
                    onCheckedChange = { isSubscribed = it }
                )
            }
        }
        
        item {
            Text(
                text = if (isSubscribed) "Subscribed Mode" else "Free Mode",
                style = MaterialTheme.typography.bodyLarge,
                color = if (isSubscribed) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        item {
            PremiumVideoSection(
                videoUrl = "https://sample-videos.com/zip/10/mp4/SampleVideo_1280x720_1mb.mp4",
                isSubscribed = isSubscribed
            )
        }
        
        item {
            PremiumVideoSection(
                videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                isSubscribed = isSubscribed
            )
        }
    }
}