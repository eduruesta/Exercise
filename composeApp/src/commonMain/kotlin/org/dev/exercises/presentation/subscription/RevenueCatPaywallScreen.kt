package org.dev.exercises.presentation.subscription

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
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
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Unable to load subscription options",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.error
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = currentState.message,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { viewModel.loadOffering() }
                    ) {
                        Text("Retry")
                    }
                }
            }
        }
    }
}
