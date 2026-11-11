package com.sigapmart.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sigapmart.app.data.model.FailedScan
import kotlinx.coroutines.flow.Flow

@Dao
interface FailedScanDao {
    @Query("SELECT * FROM failed_scans ORDER BY timestamp DESC")
    fun getAllFailedScans(): Flow<List<FailedScan>>

    @Query("SELECT * FROM failed_scans WHERE isChecked = 0 ORDER BY timestamp DESC")
    fun getUncheckedFailedScans(): Flow<List<FailedScan>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFailedScan(failedScan: FailedScan)

    @Update
    suspend fun updateFailedScan(failedScan: FailedScan)
}
