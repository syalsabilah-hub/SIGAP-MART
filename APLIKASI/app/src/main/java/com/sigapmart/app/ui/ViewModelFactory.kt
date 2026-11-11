package com.sigapmart.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sigapmart.app.SigapMartApplication
import com.sigapmart.app.ui.screens.kasir.KasirViewModel
import com.sigapmart.app.ui.screens.owner.OwnerViewModel

class ViewModelFactory(private val application: SigapMartApplication) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(KasirViewModel::class.java)) {
            return KasirViewModel(application.repository, application.userPreferences) as T
        }
        if (modelClass.isAssignableFrom(OwnerViewModel::class.java)) {
            return OwnerViewModel(application.repository, application.userPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
