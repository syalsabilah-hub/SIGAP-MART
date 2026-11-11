package com.sigapmart.app.ui.screens.kasir

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sigapmart.app.data.model.Attendance
import com.sigapmart.app.data.model.Product
import com.sigapmart.app.data.model.Transaction
import com.sigapmart.app.data.pref.UserPreferences
import com.sigapmart.app.data.repository.SigapRepository
import com.sigapmart.app.model.UserRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class CartItem(
    val product: Product,
    val quantity: Int
)

class KasirViewModel(
    private val repository: SigapRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    val userRole: StateFlow<UserRole> = userPreferences.userRole.stateIn(
        viewModelScope, SharingStarted.Lazily, UserRole.KARYAWAN
    )

    val userName: StateFlow<String> = userPreferences.userName.stateIn(
        viewModelScope, SharingStarted.Lazily, "Sinta"
    )

    val isOfflineMode: StateFlow<Boolean> = userPreferences.isOfflineMode.stateIn(
        viewModelScope, SharingStarted.Lazily, false
    )

    val themeMode: StateFlow<String> = userPreferences.themeMode.stateIn(
        viewModelScope, SharingStarted.Lazily, "SYSTEM"
    )

    val allProducts: StateFlow<List<Product>> = repository.allProducts.stateIn(
        viewModelScope, SharingStarted.Lazily, emptyList()
    )

    val allTransactions: StateFlow<List<Transaction>> = repository.allTransactions.stateIn(
        viewModelScope, SharingStarted.Lazily, emptyList()
    )

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    private val _lastTransaction = MutableStateFlow<Transaction?>(null)
    val lastTransaction: StateFlow<Transaction?> = _lastTransaction.asStateFlow()

    val totalCartAmount: StateFlow<Double> = _cartItems.combine(_cartItems) { items, _ ->
        items.sumOf { it.product.price * it.quantity }
    }.stateIn(viewModelScope, SharingStarted.Lazily, 0.0)

    fun addToCart(product: Product) {
        val currentList = _cartItems.value.toMutableList()
        val index = currentList.indexOfFirst { it.product.id == product.id }
        if (index != -1) {
            val item = currentList[index]
            currentList[index] = item.copy(quantity = item.quantity + 1)
        } else {
            currentList.add(CartItem(product = product, quantity = 1))
        }
        _cartItems.value = currentList
    }

    fun removeFromCart(product: Product) {
        val currentList = _cartItems.value.toMutableList()
        val index = currentList.indexOfFirst { it.product.id == product.id }
        if (index != -1) {
            val item = currentList[index]
            if (item.quantity > 1) {
                currentList[index] = item.copy(quantity = item.quantity - 1)
            } else {
                currentList.removeAt(index)
            }
        }
        _cartItems.value = currentList
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }

    fun processPayment(paymentMethod: String, cashierName: String, onComplete: (Transaction) -> Unit) {
        val items = _cartItems.value
        if (items.isEmpty()) return

        val total = items.sumOf { it.product.price * it.quantity }
        val summaryStr = items.joinToString(", ") { "${it.product.name} x${it.quantity}" }
        val code = "TRX-" + (100..999).random()

        val trx = Transaction(
            transactionCode = code,
            totalAmount = total,
            paymentMethod = paymentMethod,
            cashierName = cashierName,
            itemsSummary = summaryStr
        )

        viewModelScope.launch {
            repository.insertTransaction(trx)
            _lastTransaction.value = trx
            clearCart()
            onComplete(trx)
        }
    }

    fun submitAttendance(type: String) {
        viewModelScope.launch {
            repository.insertAttendance(
                Attendance(
                    employeeName = userName.value,
                    role = "Kasir",
                    type = type
                )
            )
        }
    }

    fun submitIncomingStock(productId: Long, addedQty: Int) {
        viewModelScope.launch {
            repository.allProducts.first().find { it.id == productId }?.let { prod ->
                repository.updateProduct(prod.copy(stockSystem = prod.stockSystem + addedQty, stockPhysical = prod.stockPhysical + addedQty))
            }
        }
    }

    fun submitStockOpname(productId: Long, physicalCount: Int) {
        viewModelScope.launch {
            repository.allProducts.first().find { it.id == productId }?.let { prod ->
                repository.updateProduct(prod.copy(stockPhysical = physicalCount))
            }
        }
    }

    fun addProduct(name: String, barcode: String, category: String, price: Double, costPrice: Double, stock: Int, boxSize: Int) {
        viewModelScope.launch {
            repository.insertProduct(
                Product(
                    name = name,
                    barcode = barcode,
                    category = category,
                    price = price,
                    costPrice = costPrice,
                    stockSystem = stock,
                    stockPhysical = stock,
                    itemsPerBox = boxSize
                )
            )
        }
    }

    fun toggleOfflineMode(isOffline: Boolean) {
        viewModelScope.launch {
            userPreferences.setOfflineMode(isOffline)
        }
    }

    fun setThemeMode(mode: String) {
        viewModelScope.launch {
            userPreferences.setThemeMode(mode)
        }
    }

    fun setRole(role: UserRole) {
        viewModelScope.launch {
            userPreferences.setUserRole(role)
        }
    }
}
