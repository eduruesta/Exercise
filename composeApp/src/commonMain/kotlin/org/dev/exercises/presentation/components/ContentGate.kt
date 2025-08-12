package org.dev.exercises.presentation.components

import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import org.dev.exercises.data.subscription.EntitlementHelper
import org.dev.exercises.data.subscription.RevenueCatConfig

/**
 * ContentGate following RevenueCat documentation pattern for validating entitlements
 * 
 * Example usage from docs:
 * val ENTITLEMENT_IDENTIFIER = ".." // get specific entitlement identifier from your RevenueCat dashboard
 * val customerInfo = Purchases.sharedInstance.awaitCustomerInfo()
 * val isEntitled = customerInfo?.entitlements[ENTITLEMENT_IDENTIFIER]?.isActive == true
 */
@Composable
fun ContentGate(
    entitlementIdentifier: String = RevenueCatConfig.Entitlements.PREMIUM,
    premiumContent: @Composable () -> Unit,
    freeContent: @Composable () -> Unit
) {
    var isEntitled by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    
    LaunchedEffect(entitlementIdentifier) {
        scope.launch {
            isEntitled = EntitlementHelper.validateEntitlement(entitlementIdentifier)
            isLoading = false
        }
    }
    
    if (isLoading) {
        // You can show a loading state here if needed
        freeContent()
    } else if (isEntitled) {
        premiumContent()
    } else {
        freeContent()
    }
}

/**
 * Example implementation from RevenueCat docs:
 * @Composable
 * fun ContentScreen(isEntitled: Boolean) {
 *     if (isEntitled) {
 *       // if the user is granted access to this entitlement. don't need to display a banner.
 *     } else {
 *       // display a banner UI here or display a paywall
 *       ..
 *     }
 * }
 */
@Composable
fun ExampleContentScreen() {
    ContentGate(
        premiumContent = {
            // Premium content without ads
        },
        freeContent = {
            // Free content with ads or paywall prompt
        }
    )
}