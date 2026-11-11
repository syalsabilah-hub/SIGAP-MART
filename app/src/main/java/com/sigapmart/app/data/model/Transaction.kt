package com.sigapmart.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val transactionCode: String,
    val timestamp: Long = System.currentTimeMillis(),
    val totalAmount: Double,
    val paymentMethod: String, // "TUNAI", "QRIS", "DEBIT", "ONLINE"
    val cashierName: String,
    val itemsSummary: String,
    val isCancellationRequested: Boolean = false,
    val cancellationStatus: String = "NONE" // "NONE", "PENDING", "APPROVED", "REJECTED"
)
