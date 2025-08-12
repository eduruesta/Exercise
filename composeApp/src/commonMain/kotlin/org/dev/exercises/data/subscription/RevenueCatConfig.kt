package org.dev.exercises.data.subscription

object RevenueCatConfig {
    // Replace with your actual RevenueCat API key
    // For production, consider using BuildKonfig or similar to manage API keys per build variant
    const val API_KEY = "appl_FzZIRFrBEKNResIfQudKSvdYAIP"
    
    // Product identifiers
    object Products {
        const val PREMIUM_MONTHLY = "premium_monthly"
        const val PREMIUM_YEARLY = "premium_yearly"
        const val FULL_SUBSCRIPTION = "full_subscription"

    }
    
    // Entitlement identifiers
    object Entitlements {
        const val PREMIUM = "premium"
    }
}