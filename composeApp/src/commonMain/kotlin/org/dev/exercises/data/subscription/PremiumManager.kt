package org.dev.exercises.data.subscription

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.koin.compose.koinInject

/**
 * Utility class to easily check premium status throughout the app
 */
object PremiumManager {
    
    @Composable
    fun isPremiumUser(): Boolean {
        val revenueCatManager: RevenueCatManager = koinInject()
        val isPremiumActive by revenueCatManager.isPremiumActive.collectAsState()
        return isPremiumActive
    }
    
    @Composable
    fun isUserSubscribed(): Boolean {
        val revenueCatManager: RevenueCatManager = koinInject()
        val isSubscribed by revenueCatManager.isUserSubscribed.collectAsState()
        return isSubscribed
    }
    
    fun checkEntitlement(revenueCatManager: RevenueCatManager, entitlementId: String): Boolean {
        return revenueCatManager.checkEntitlement(entitlementId)
    }
    
    suspend fun checkEntitlementSuspend(revenueCatManager: RevenueCatManager, entitlementId: String): Boolean {
        val customerInfo = revenueCatManager.getCustomerInfo().getOrNull()
        return customerInfo?.entitlements?.get(entitlementId)?.isActive == true
    }
}

/**
 * Composable function to conditionally show content based on premium status
 */
@Composable
fun PremiumGate(
    showPremiumContent: @Composable () -> Unit,
    showFreeContent: @Composable () -> Unit
) {
    if (PremiumManager.isPremiumUser()) {
        showPremiumContent()
    } else {
        showFreeContent()
    }
}