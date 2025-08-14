package org.dev.exercises.presentation.subscription

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.dev.exercises.data.subscription.RevenueCatManager
import com.revenuecat.purchases.kmp.Purchases
import com.revenuecat.purchases.kmp.ktx.awaitOfferings
import com.revenuecat.purchases.kmp.models.Offering
import com.revenuecat.purchases.kmp.models.PurchasesException

sealed class RevenueCatPaywallState {
    object Loading : RevenueCatPaywallState()
    data class Success(val offering: Offering) : RevenueCatPaywallState()
    data class Error(val message: String) : RevenueCatPaywallState()
}

class RevenueCatPaywallViewModel(
    private val revenueCatManager: RevenueCatManager
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<RevenueCatPaywallState>(RevenueCatPaywallState.Loading)
    val uiState: StateFlow<RevenueCatPaywallState> = _uiState.asStateFlow()
    
    private val _isUserSubscribed = MutableStateFlow(false)
    val isUserSubscribed: StateFlow<Boolean> = _isUserSubscribed.asStateFlow()
    
    init {
        observeSubscriptionStatus()
        // Don't load offering immediately, wait for explicit call
    }
    
    private fun observeSubscriptionStatus() {
        viewModelScope.launch {
            revenueCatManager.isUserSubscribed.collect { isSubscribed ->
                _isUserSubscribed.value = isSubscribed
            }
        }
    }
    
    fun loadOffering() {
        viewModelScope.launch {
            _uiState.value = RevenueCatPaywallState.Loading
            
            try {
                val offerings = Purchases.sharedInstance.awaitOfferings()
                offerings.current?.let { currentOffering ->
                    _uiState.value = RevenueCatPaywallState.Success(currentOffering)
                } ?: run {
                    _uiState.value = RevenueCatPaywallState.Error("No current offering available")
                }
            } catch (e: PurchasesException) {
                _uiState.value = RevenueCatPaywallState.Error(
                    e.message
                )
            }
        }
    }
}