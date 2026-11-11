package com.sigapmart.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sigapmart.app.data.model.Attendance
import com.sigapmart.app.data.model.AuditLog
import com.sigapmart.app.data.model.FailedScan
import com.sigapmart.app.data.model.Product
import com.sigapmart.app.data.model.Transaction

@Database(
    entities = [
        Product::class,
        Transaction::class,
        Attendance::class,
        FailedScan::class,
        AuditLog::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun transactionDao(): TransactionDao
    abstract fun attendanceDao(): AttendanceDao
    abstract fun failedScanDao(): FailedScanDao
    abstract fun auditLogDao(): AuditLogDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "sigap_mart_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
