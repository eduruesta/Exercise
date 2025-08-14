package org.dev.exercises.presentation.subscription

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import com.revenuecat.purchases.kmp.ui.revenuecatui.PaywallOptions
import com.revenuecat.purchases.kmp.ui.revenuecatui.Paywall

@Composable
fun RevenueCatPaywallScreen(
    onBackClick: () -> Unit,
    onSubscriptionSuccess: () -> Unit
) {
    val viewModel: RevenueCatPaywallViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val isUserSubscribed by viewModel.isUserSubscribed.collectAsState()

    // Initialize RevenueCat and load offering
    LaunchedEffect(Unit) {
        viewModel.loadOffering()
    }

    // Navigate back if user becomes subscribed
    LaunchedEffect(isUserSubscribed) {
        if (isUserSubscribed) {
            onSubscriptionSuccess()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        // RevenueCat Paywall UI
        when (val currentState = uiState) {
            is RevenueCatPaywallState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is RevenueCatPaywallState.Success -> {
                val options = remember {
                    PaywallOptions(
                        dismissRequest = { onBackClick() }
                    ) {
                        offering = currentState.offering
                        shouldDisplayDismissButton = true
                    }
                }

                Paywall(options)
            }

            is RevenueCatPaywallState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Transparent
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.1f),
                                            MaterialTheme.colorScheme.surface,
                                            MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.05f)
                                        )
                                    ),
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .clip(RoundedCornerShape(16.dp))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                // Error emoji with background
                                Box(
                                    modifier = Modifier
                                        .size(80.dp)
                                        .background(
                                            MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.2f),
                                            RoundedCornerShape(40.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "😔",
                                        style = MaterialTheme.typography.displayMedium
                                    )
                                }

                                Text(
                                    text = "Oops! Something went wrong",
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    textAlign = TextAlign.Center
                                )

                                Text(
                                    text = "We couldn't load the subscription options right now. Please check your connection and try again.",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center,
                                    lineHeight = MaterialTheme.typography.bodyLarge.lineHeight
                                )

                                if (currentState.message.isNotBlank()) {
                                    Card(
                                        colors = CardDefaults.cardColors(
                                            containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.1f)
                                        )
                                    ) {
                                        Text(
                                            text = currentState.message,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onErrorContainer,
                                            modifier = Modifier.padding(12.dp),
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }

                                Button(
                                    onClick = { viewModel.loadOffering() },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary
                                    ),
                                    shape = RoundedCornerShape(24.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text(
                                            text = "🔄",
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                        Text(
                                            text = "Try Again",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
