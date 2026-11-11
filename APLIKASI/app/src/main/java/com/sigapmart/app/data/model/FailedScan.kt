package com.sigapmart.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "failed_scans")
data class FailedScan(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val barcode: String,
    val productName: String = "Barang Tidak Dikenal",
    val cashierName: String,
    val timestamp: Long = System.currentTimeMillis(),
    val estimatedValue: Double = 0.0,
    val isChecked: Boolean = false
)
