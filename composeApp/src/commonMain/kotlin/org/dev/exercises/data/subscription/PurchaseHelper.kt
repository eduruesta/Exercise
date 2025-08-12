package org.dev.exercises.data.subscription

import com.revenuecat.purchases.kmp.Purchases
import com.revenuecat.purchases.kmp.models.PurchasesException
import com.revenuecat.purchases.kmp.ktx.awaitGetProducts
import com.revenuecat.purchases.kmp.ktx.awaitPurchase
import com.revenuecat.purchases.kmp.models.StoreProduct

/**
 * Helper object for implementing in-app purchases following RevenueCat documentation patterns
 */
object PurchaseHelper {
    
    /**
     * Fetches product information following RevenueCat pattern:
     * val products = Purchases.sharedInstance.awaitGetProducts(productIds = listOf("paywall_tester.subs"))
     */
    suspend fun fetchProducts(productIds: List<String>): Result<List<StoreProduct>> {
        return try {
            val products = Purchases.sharedInstance.awaitGetProducts(productIds = productIds)
            Result.success(products)
        } catch (e: PurchasesException) {
            println("Error fetching products: ${e.message}")
            Result.failure(e)
        }
    }
    
    /**
     * Fetches products using RevenueCat config product IDs
     */
    suspend fun fetchConfiguredProducts(): Result<List<StoreProduct>> {
        val productIds = listOf(
            RevenueCatConfig.Products.FULL_SUBSCRIPTION
        )
        return fetchProducts(productIds)
    }
    
    /**
     * Initiates purchase following RevenueCat pattern:
     * val purchaseResult = Purchases.sharedInstance.awaitPurchase(storeProduct = products.first())
     */
    suspend fun purchaseProduct(storeProduct: StoreProduct): Result<Boolean> {
        return try {
            val purchaseResult = Purchases.sharedInstance.awaitPurchase(storeProduct = storeProduct)
            val isSuccessful = purchaseResult.customerInfo.entitlements.active.isNotEmpty()
            Result.success(isSuccessful)
        } catch (e: PurchasesException) {
            println("Error during purchase: ${e.message}")
            Result.failure(e)
        }
    }
    
    /**
     * Purchase product by ID - fetches product info and then purchases
     */
    suspend fun purchaseProductById(productId: String): Result<Boolean> {
        return try {
            val products = fetchProducts(listOf(productId)).getOrThrow()
            if (products.isNotEmpty()) {
                purchaseProduct(products.first())
            } else {
                Result.failure(Exception("Product not found: $productId"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}