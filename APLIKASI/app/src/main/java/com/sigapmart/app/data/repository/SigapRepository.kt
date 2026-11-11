package com.sigapmart.app.data.repository

import com.sigapmart.app.data.local.AppDatabase
import com.sigapmart.app.data.model.Attendance
import com.sigapmart.app.data.model.AuditLog
import com.sigapmart.app.data.model.FailedScan
import com.sigapmart.app.data.model.Product
import com.sigapmart.app.data.model.Transaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class SigapRepository(private val db: AppDatabase) {

    val allProducts: Flow<List<Product>> = db.productDao().getAllProducts()
    val allTransactions: Flow<List<Transaction>> = db.transactionDao().getAllTransactions()
    val allAttendances: Flow<List<Attendance>> = db.attendanceDao().getAllAttendances()
    val allFailedScans: Flow<List<FailedScan>> = db.failedScanDao().getAllFailedScans()
    val uncheckedFailedScans: Flow<List<FailedScan>> = db.failedScanDao().getUncheckedFailedScans()
    val pendingCancellations: Flow<List<Transaction>> = db.transactionDao().getPendingCancellationRequests()
    val auditLogs: Flow<List<AuditLog>> = db.auditLogDao().getAllAuditLogs()

    suspend fun getProductByBarcode(barcode: String): Product? {
        return db.productDao().getProductByBarcode(barcode)
    }

    suspend fun insertProduct(product: Product) {
        db.productDao().insertProduct(product)
        db.auditLogDao().insertAuditLog(
            AuditLog(user = "Sistem", action = "Menambah/mengedit produk: ${product.name}", category = "HARGA")
        )
    }

    suspend fun updateProduct(product: Product) {
        db.productDao().updateProduct(product)
    }

    suspend fun insertTransaction(transaction: Transaction) {
        db.transactionDao().insertTransaction(transaction)
        db.auditLogDao().insertAuditLog(
            AuditLog(user = transaction.cashierName, action = "Transaksi Selesai #${transaction.transactionCode}", category = "TRANSAKSI")
        )
    }

    suspend fun updateTransaction(transaction: Transaction) {
        db.transactionDao().updateTransaction(transaction)
    }

    suspend fun insertAttendance(attendance: Attendance) {
        db.attendanceDao().insertAttendance(attendance)
        db.auditLogDao().insertAuditLog(
            AuditLog(user = attendance.employeeName, action = "Absensi ${attendance.type}", category = "ABSENSI")
        )
    }

    suspend fun insertFailedScan(failedScan: FailedScan) {
        db.failedScanDao().insertFailedScan(failedScan)
    }

    suspend fun markFailedScanChecked(failedScan: FailedScan) {
        db.failedScanDao().updateFailedScan(failedScan.copy(isChecked = true))
    }

    suspend fun seedMockDataIfEmpty() {
        val existingProducts = db.productDao().getAllProducts().first()
        if (existingProducts.isEmpty()) {
            val initialProducts = listOf(
                Product(barcode = "8991234567890", name = "Minyak Goreng 2 L", category = "Sembako", price = 34000.0, costPrice = 28000.0, stockSystem = 64, stockPhysical = 60, itemsPerBox = 12),
                Product(barcode = "8992345678901", name = "Beras 5 kg", category = "Sembako", price = 68000.0, costPrice = 59000.0, stockSystem = 58, stockPhysical = 58, itemsPerBox = 4),
                Product(barcode = "8993456789012", name = "Air Mineral 600 ml", category = "Minuman", price = 4000.0, costPrice = 2500.0, stockSystem = 40, stockPhysical = 38, itemsPerBox = 24),
                Product(barcode = "8994567890123", name = "Mi Instan Goreng", category = "Makanan", price = 3100.0, costPrice = 2600.0, stockSystem = 30, stockPhysical = 30, itemsPerBox = 40),
                Product(barcode = "8995678901234", name = "Susu UHT 1 L", category = "Minuman", price = 18500.0, costPrice = 15000.0, stockSystem = 12, stockPhysical = 10, itemsPerBox = 12)
            )
            db.productDao().insertAllProducts(initialProducts)

            val initialTransactions = listOf(
                Transaction(transactionCode = "TRX-101", totalAmount = 102000.0, paymentMethod = "TUNAI", cashierName = "Sinta", itemsSummary = "Minyak Goreng 2L x3"),
                Transaction(transactionCode = "TRX-102", totalAmount = 68000.0, paymentMethod = "QRIS", cashierName = "Sinta", itemsSummary = "Beras 5kg x1"),
                Transaction(transactionCode = "TRX-103", totalAmount = 37000.0, paymentMethod = "KARTU DEBIT", cashierName = "Budi", itemsSummary = "Susu UHT 1L x2"),
                Transaction(transactionCode = "TRX-104", totalAmount = 15500.0, paymentMethod = "ONLINE", cashierName = "Sinta", itemsSummary = "Mi Instan x5")
            )
            for (t in initialTransactions) {
                db.transactionDao().insertTransaction(t)
            }

            db.failedScanDao().insertFailedScan(
                FailedScan(barcode = "8990001112223", productName = "Rokok Sebungkus", cashierName = "Kasir Sinta", estimatedValue = 28000.0)
            )
            db.failedScanDao().insertFailedScan(
                FailedScan(barcode = "8999998887776", productName = "Barang Tidak Dikenal", cashierName = "Kasir Budi", estimatedValue = 15000.0)
            )

            db.attendanceDao().insertAttendance(
                Attendance(employeeName = "Sinta", role = "Kasir", type = "MASUK", status = "TEPAT_WAKTU")
            )
            db.attendanceDao().insertAttendance(
                Attendance(employeeName = "Budi", role = "Kasir", type = "MASUK", status = "TEPAT_WAKTU")
            )

            db.auditLogDao().insertAuditLog(
                AuditLog(user = "Sinta", action = "Sinta menyelesaikan stok opname", category = "STOK")
            )
            db.auditLogDao().insertAuditLog(
                AuditLog(user = "Budi", action = "Melakukan transaksi TRX-103", category = "TRANSAKSI")
            )
        }
    }
}
