package com.sigapmart.app.ui.screens.owner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sigapmart.app.data.model.Attendance
import com.sigapmart.app.data.model.AuditLog
import com.sigapmart.app.data.model.FailedScan
import com.sigapmart.app.data.model.Product
import com.sigapmart.app.data.model.Transaction
import com.sigapmart.app.data.pref.UserPreferences
import com.sigapmart.app.data.repository.SigapRepository
import com.sigapmart.app.model.UserRole
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class OwnerViewModel(
    private val repository: SigapRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    val userRole: StateFlow<UserRole> = userPreferences.userRole.stateIn(
        viewModelScope, SharingStarted.Lazily, UserRole.OWNER
    )

    val themeMode: StateFlow<String> = userPreferences.themeMode.stateIn(
        viewModelScope, SharingStarted.Lazily, "SYSTEM"
    )

    val isOfflineMode: StateFlow<Boolean> = userPreferences.isOfflineMode.stateIn(
        viewModelScope, SharingStarted.Lazily, false
    )

    val allProducts: StateFlow<List<Product>> = repository.allProducts.stateIn(
        viewModelScope, SharingStarted.Lazily, emptyList()
    )

    val allTransactions: StateFlow<List<Transaction>> = repository.allTransactions.stateIn(
        viewModelScope, SharingStarted.Lazily, emptyList()
    )

    val allAttendances: StateFlow<List<Attendance>> = repository.allAttendances.stateIn(
        viewModelScope, SharingStarted.Lazily, emptyList()
    )

    val allFailedScans: StateFlow<List<FailedScan>> = repository.allFailedScans.stateIn(
        viewModelScope, SharingStarted.Lazily, emptyList()
    )

    val auditLogs: StateFlow<List<AuditLog>> = repository.auditLogs.stateIn(
        viewModelScope, SharingStarted.Lazily, emptyList()
    )

    fun markFailedScanChecked(scan: FailedScan) {
        viewModelScope.launch {
            repository.markFailedScanChecked(scan)
        }
    }

    fun updateProductPriceAndBox(product: Product, newPrice: Double, newBoxSize: Int) {
        viewModelScope.launch {
            repository.updateProduct(product.copy(price = newPrice, itemsPerBox = newBoxSize))
        }
    }

    fun approveCancellation(transaction: Transaction) {
        viewModelScope.launch {
            repository.updateTransaction(transaction.copy(cancellationStatus = "APPROVED", isCancellationRequested = false))
        }
    }

    fun rejectCancellation(transaction: Transaction) {
        viewModelScope.launch {
            repository.updateTransaction(transaction.copy(cancellationStatus = "REJECTED", isCancellationRequested = false))
        }
    }

    fun setRole(role: UserRole) {
        viewModelScope.launch {
            userPreferences.setUserRole(role)
        }
    }

    fun setThemeMode(mode: String) {
        viewModelScope.launch {
            userPreferences.setThemeMode(mode)
        }
    }

    fun toggleOfflineMode(isOffline: Boolean) {
        viewModelScope.launch {
            userPreferences.setOfflineMode(isOffline)
        }
    }
}
