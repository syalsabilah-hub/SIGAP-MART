package com.sigapmart.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val barcode: String,
    val name: String,
    val category: String,
    val price: Double,
    val costPrice: Double,
    val stockSystem: Int,
    val stockPhysical: Int = stockSystem,
    val itemsPerBox: Int = 1,
    val imageUrl: String = ""
)
