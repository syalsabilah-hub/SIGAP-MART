package com.sigapmart.app

import android.app.Application
import com.sigapmart.app.data.local.AppDatabase
import com.sigapmart.app.data.pref.UserPreferences
import com.sigapmart.app.data.repository.SigapRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SigapMartApplication : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { SigapRepository(database) }
    val userPreferences by lazy { UserPreferences(this) }

    override fun onCreate() {
        super.onCreate()
        CoroutineScope(Dispatchers.IO).launch {
            repository.seedMockDataIfEmpty()
        }
    }
}
