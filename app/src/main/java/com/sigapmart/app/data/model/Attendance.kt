package com.sigapmart.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "attendances")
data class Attendance(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val employeeName: String,
    val role: String,
    val timestamp: Long = System.currentTimeMillis(),
    val type: String, // "MASUK", "PULANG"
    val status: String = "TEPAT_WAKTU" // "TEPAT_WAKTU", "TERLAMBAT"
)
