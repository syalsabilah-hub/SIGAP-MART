package com.sigapmart.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import com.sigapmart.app.ui.ViewModelFactory
import com.sigapmart.app.ui.navigation.SigapMartNavGraph
import com.sigapmart.app.ui.screens.kasir.KasirViewModel
import com.sigapmart.app.ui.screens.owner.OwnerViewModel
import com.sigapmart.app.ui.theme.SIGAPMARTTheme

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

class MainActivity : ComponentActivity() {

    private val kasirViewModel: KasirViewModel by viewModels {
        ViewModelFactory(application as SigapMartApplication)
    }

    private val ownerViewModel: OwnerViewModel by viewModels {
        ViewModelFactory(application as SigapMartApplication)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeMode by kasirViewModel.themeMode.collectAsState()
            SIGAPMARTTheme(themeMode = themeMode) {
                val navController = rememberNavController()
                SigapMartNavGraph(
                    navController = navController,
                    kasirViewModel = kasirViewModel,
                    ownerViewModel = ownerViewModel
                )
            }
        }
    }
}
