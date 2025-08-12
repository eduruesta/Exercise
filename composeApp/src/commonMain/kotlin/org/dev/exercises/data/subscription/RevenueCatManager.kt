package org.dev.exercises.data.subscription

import com.revenuecat.purchases.kmp.LogLevel
import com.revenuecat.purchases.kmp.Purchases
import com.revenuecat.purchases.kmp.configure
import com.revenuecat.purchases.kmp.ktx.awaitCustomerInfo
import com.revenuecat.purchases.kmp.ktx.awaitOfferings
import com.revenuecat.purchases.kmp.ktx.awaitPurchase
import com.revenuecat.purchases.kmp.models.PurchasesException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.revenuecat.purchases.kmp.models.CustomerInfo as RCCustomerInfo
import com.revenuecat.purchases.kmp.models.Offering as RCOffering
import com.revenuecat.purchases.kmp.models.Package as RCPackage
import com.revenuecat.purchases.kmp.models.StoreProduct as RCStoreProduct

// Wrapper models to maintain existing interface
data class CustomerInfo(
    val entitlements: Map<String, Entitlement> = emptyMap(),
    val original: RCCustomerInfo? = null
)

data class Entitlement(
    val isActive: Boolean = false
)

data class Offering(
    val identifier: String,
    val serverDescription: String,
    val availablePackages: List<Package>,
    val original: RCOffering? = null
)

data class Package(
    val identifier: String,
    val packageType: String,
    val storeProduct: StoreProduct,
    val original: RCPackage? = null
)

data class StoreProduct(
    val id: String,
    val title: String,
    val price: String,
    val original: RCStoreProduct? = null
)

interface RevenueCatManager {
    val customerInfo: StateFlow<CustomerInfo?>
    val isUserSubscribed: StateFlow<Boolean>
    val isPremiumActive: StateFlow<Boolean>
    
    suspend fun initialize(apiKey: String)
    suspend fun getOfferings(): Result<List<Offering>>
    suspend fun purchasePackage(packageToPurchase: Package): Result<CustomerInfo>
    suspend fun restorePurchases(): Result<CustomerInfo>
    suspend fun getCustomerInfo(): Result<CustomerInfo>
    fun checkEntitlement(entitlementId: String): Boolean
}

class RevenueCatManagerImpl : RevenueCatManager {
    
    private val _customerInfo = MutableStateFlow<CustomerInfo?>(null)
    override val customerInfo: StateFlow<CustomerInfo?> = _customerInfo.asStateFlow()
    
    private val _isUserSubscribed = MutableStateFlow(false)
    override val isUserSubscribed: StateFlow<Boolean> = _isUserSubscribed.asStateFlow()
    
    private val _isPremiumActive = MutableStateFlow(false)
    override val isPremiumActive: StateFlow<Boolean> = _isPremiumActive.asStateFlow()
    
    private var isInitialized = false
    
    override suspend fun initialize(apiKey: String) {
        try {
            if (!isInitialized) {
                Purchases.logLevel = LogLevel.DEBUG
                Purchases.configure(apiKey = apiKey) {
                    // Optional: set app user ID
                    // appUserId = "<app_user_id>"
                }
                isInitialized = true
                
                // Load initial customer info
                updateCustomerInfo()
            }
        } catch (e: Exception) {
            println("RevenueCat initialization error: ${e.message}")
        }
    }
    
    override suspend fun getOfferings(): Result<List<Offering>> {
        return try {
            if (!isInitialized) {
                return Result.failure(Exception("RevenueCat not initialized. Call initialize() first."))
            }
            val offerings = Purchases.sharedInstance.awaitOfferings()
            val convertedOfferings = offerings.all.values.map { rcOffering ->
                Offering(
                    identifier = rcOffering.identifier,
                    serverDescription = rcOffering.serverDescription,
                    availablePackages = rcOffering.availablePackages.map { rcPackage ->
                        Package(
                            identifier = rcPackage.identifier,
                            packageType = rcPackage.packageType.name,
                            storeProduct = StoreProduct(
                                id = rcPackage.storeProduct.id,
                                title = rcPackage.storeProduct.title,
                                price = rcPackage.storeProduct.price.formatted,
                                original = rcPackage.storeProduct
                            ),
                            original = rcPackage
                        )
                    },
                    original = rcOffering
                )
            }
            Result.success(convertedOfferings)
        } catch (e: PurchasesException) {
            println("RevenueCat get offerings error: ${e.message}")
            Result.failure(e)
        }
    }
    
    override suspend fun purchasePackage(packageToPurchase: Package): Result<CustomerInfo> {
        return try {
            val rcPackage = packageToPurchase.original
                ?: return Result.failure(Exception("Original package not found"))
            
            val purchaseResult = Purchases.sharedInstance.awaitPurchase(rcPackage)
            val convertedCustomerInfo = convertCustomerInfo(purchaseResult.customerInfo)
            updateCustomerInfoState(convertedCustomerInfo)
            Result.success(convertedCustomerInfo)
        } catch (e: PurchasesException) {
            println("RevenueCat purchase error: ${e.message}")
            Result.failure(e)
        }
    }
    
    override suspend fun restorePurchases(): Result<CustomerInfo> {
        return try {
            // For now, just get updated customer info after restore attempt
            // The actual restore happens in the background
            Purchases.sharedInstance.restorePurchases(
                onError = { error ->
                    println("RevenueCat restore purchases error: ${error.message}")
                },
                onSuccess = { customerInfo ->
                    println("RevenueCat restore purchases success")
                }
            )
            
            // Get updated customer info
            val customerInfo = Purchases.sharedInstance.awaitCustomerInfo()
            val convertedCustomerInfo = convertCustomerInfo(customerInfo)
            updateCustomerInfoState(convertedCustomerInfo)
            Result.success(convertedCustomerInfo)
        } catch (e: Exception) {
            println("RevenueCat restore purchases error: ${e.message}")
            Result.failure(e)
        }
    }
    
    override suspend fun getCustomerInfo(): Result<CustomerInfo> {
        return try {
            val customerInfo = Purchases.sharedInstance.awaitCustomerInfo()
            val convertedCustomerInfo = convertCustomerInfo(customerInfo)
            Result.success(convertedCustomerInfo)
        } catch (e: PurchasesException) {
            println("RevenueCat get customer info error: ${e.message}")
            Result.failure(e)
        }
    }
    
    override fun checkEntitlement(entitlementId: String): Boolean {
        val currentCustomerInfo = _customerInfo.value
        return currentCustomerInfo?.entitlements?.get(entitlementId)?.isActive == true
    }
    
    private suspend fun updateCustomerInfo() {
        try {
            val result = getCustomerInfo()
            result.getOrNull()?.let { customerInfo ->
                updateCustomerInfoState(customerInfo)
            }
        } catch (e: Exception) {
            println("Failed to update customer info: ${e.message}")
        }
    }
    
    private fun convertCustomerInfo(rcCustomerInfo: RCCustomerInfo): CustomerInfo {
        val entitlements = rcCustomerInfo.entitlements.active.mapValues { (_, entitlement) ->
            Entitlement(isActive = true)
        }
        
        return CustomerInfo(
            entitlements = entitlements,
            original = rcCustomerInfo
        )
    }
    
    private fun updateCustomerInfoState(customerInfo: CustomerInfo) {
        _customerInfo.value = customerInfo
        _isUserSubscribed.value = customerInfo.entitlements.values.any { it.isActive }
        _isPremiumActive.value = customerInfo.entitlements[RevenueCatConfig.Entitlements.PREMIUM]?.isActive == true
    }
}