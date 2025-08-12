package org.dev.exercises.data.subscription

import com.revenuecat.purchases.kmp.Purchases
import com.revenuecat.purchases.kmp.ktx.awaitCustomerInfo
import com.revenuecat.purchases.kmp.models.PurchasesException

/**
 * Helper object for validating entitlements following RevenueCat best practices
 */
object EntitlementHelper {
    
    /**
     * Validates if user has active entitlement using RevenueCat pattern:
     * val customerInfo = Purchases.sharedInstance.awaitCustomerInfo()
     * val isEntitled = customerInfo?.entitlements[ENTITLEMENT_IDENTIFIER]?.isActive == true
     */
    suspend fun validateEntitlement(entitlementIdentifier: String): Boolean {
        return try {
            val customerInfo = Purchases.sharedInstance.awaitCustomerInfo()
            customerInfo.entitlements.active[entitlementIdentifier]?.isActive == true
        } catch (e: PurchasesException) {
            println("Error validating entitlement: ${e.message}")
            false
        }
    }
    
    /**
     * Validates premium entitlement specifically
     */
    suspend fun validatePremiumEntitlement(): Boolean {
        return validateEntitlement(RevenueCatConfig.Entitlements.PREMIUM)
    }
    
    /**
     * Checks if user has any active entitlements
     */
    suspend fun hasAnyActiveEntitlement(): Boolean {
        return try {
            val customerInfo = Purchases.sharedInstance.awaitCustomerInfo()
            customerInfo.entitlements.active.isNotEmpty()
        } catch (e: PurchasesException) {
            println("Error checking active entitlements: ${e.message}")
            false
        }
    }
}