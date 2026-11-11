package com.sigapmart.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sigapmart.app.model.UserRole
import com.sigapmart.app.ui.screens.kasir.AbsensiScreen
import com.sigapmart.app.ui.screens.kasir.BarangMasukScreen
import com.sigapmart.app.ui.screens.kasir.BerandaKasirScreen
import com.sigapmart.app.ui.screens.kasir.DaftarProdukScreen
import com.sigapmart.app.ui.screens.kasir.KasirNotifikasiScreen
import com.sigapmart.app.ui.screens.kasir.KasirProfilPengaturanScreen
import com.sigapmart.app.ui.screens.kasir.KasirViewModel
import com.sigapmart.app.ui.screens.kasir.LoginScreen
import com.sigapmart.app.ui.screens.kasir.OnboardingScreen
import com.sigapmart.app.ui.screens.kasir.PembayaranScreen
import com.sigapmart.app.ui.screens.kasir.ProdukBaruScreen
import com.sigapmart.app.ui.screens.kasir.RiwayatTransaksiScreen
import com.sigapmart.app.ui.screens.kasir.ScanKeranjangScreen
import com.sigapmart.app.ui.screens.kasir.SplashScreen
import com.sigapmart.app.ui.screens.kasir.StokOpnameScreen
import com.sigapmart.app.ui.screens.kasir.StrukDigitalScreen
import com.sigapmart.app.ui.screens.owner.DashboardOwnerScreen
import com.sigapmart.app.ui.screens.owner.EditHargaDusScreen
import com.sigapmart.app.ui.screens.owner.KeuntunganDetailScreen
import com.sigapmart.app.ui.screens.owner.LogPersetujuanScreen
import com.sigapmart.app.ui.screens.owner.MonitoringAbsensiScreen
import com.sigapmart.app.ui.screens.owner.OwnerNotifikasiScreen
import com.sigapmart.app.ui.screens.owner.OwnerProfilPengaturanScreen
import com.sigapmart.app.ui.screens.owner.OwnerViewModel
import com.sigapmart.app.ui.screens.owner.ScanTidakBerhasilScreen
import com.sigapmart.app.ui.screens.owner.SelisihStokScreen

object Destinations {
    const val SPLASH = "splash"
    const val ONBOARDING = "onboarding"
    const val LOGIN = "login"

    // Kasir Flow
    const val KASIR_BERANDA = "kasir_beranda"
    const val KASIR_SCAN = "kasir_scan"
    const val KASIR_PEMBAYARAN = "kasir_pembayaran"
    const val KASIR_STRUK = "kasir_struk"
    const val KASIR_BARANG_MASUK = "kasir_barang_masuk"
    const val KASIR_STOK_OPNAME = "kasir_stok_opname"
    const val KASIR_ABSENSI = "kasir_absensi"
    const val KASIR_DAFTAR_PRODUK = "kasir_daftar_produk"
    const val KASIR_PRODUK_BARU = "kasir_produk_baru"
    const val KASIR_RIWAYAT = "kasir_riwayat"
    const val KASIR_NOTIFIKASI = "kasir_notifikasi"
    const val KASIR_PROFIL = "kasir_profil"

    // Owner Flow
    const val OWNER_DASHBOARD = "owner_dashboard"
    const val OWNER_KEUNTUNGAN = "owner_keuntungan"
    const val OWNER_ABSENSI = "owner_absensi"
    const val OWNER_SCAN_BERMASALAH = "owner_scan_bermasalah"
    const val OWNER_SELISIH_STOK = "owner_selisih_stok"
    const val OWNER_EDIT_HARGA = "owner_edit_harga"
    const val OWNER_LOG_PERSETUJUAN = "owner_log_persetujuan"
    const val OWNER_NOTIFIKASI = "owner_notifikasi"
    const val OWNER_PROFIL = "owner_profil"
}

@Composable
fun SigapMartNavGraph(
    navController: NavHostController,
    kasirViewModel: KasirViewModel,
    ownerViewModel: OwnerViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Destinations.SPLASH
    ) {
        composable(Destinations.SPLASH) {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate(Destinations.ONBOARDING) {
                        popUpTo(Destinations.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        composable(Destinations.ONBOARDING) {
            OnboardingScreen(
                onFinishOnboarding = {
                    navController.navigate(Destinations.LOGIN) {
                        popUpTo(Destinations.ONBOARDING) { inclusive = true }
                    }
                }
            )
        }

        composable(Destinations.LOGIN) {
            LoginScreen(
                onLoginSuccess = { role ->
                    if (role == UserRole.OWNER) {
                        ownerViewModel.setRole(UserRole.OWNER)
                        navController.navigate(Destinations.OWNER_DASHBOARD) {
                            popUpTo(Destinations.LOGIN) { inclusive = true }
                        }
                    } else {
                        kasirViewModel.setRole(UserRole.KARYAWAN)
                        navController.navigate(Destinations.KASIR_BERANDA) {
                            popUpTo(Destinations.LOGIN) { inclusive = true }
                        }
                    }
                }
            )
        }

        // --- KASIR SCREENS ---
        composable(Destinations.KASIR_BERANDA) {
            BerandaKasirScreen(
                viewModel = kasirViewModel,
                onNavigateToScan = { navController.navigate(Destinations.KASIR_SCAN) },
                onNavigateToBarangMasuk = { navController.navigate(Destinations.KASIR_BARANG_MASUK) },
                onNavigateToStokOpname = { navController.navigate(Destinations.KASIR_STOK_OPNAME) },
                onNavigateToAbsensi = { navController.navigate(Destinations.KASIR_ABSENSI) },
                onNavigateToDaftarProduk = { navController.navigate(Destinations.KASIR_DAFTAR_PRODUK) },
                onNavigateToRiwayat = { navController.navigate(Destinations.KASIR_RIWAYAT) },
                onNavigateToNotifikasi = { navController.navigate(Destinations.KASIR_NOTIFIKASI) },
                onNavigateToProfil = { navController.navigate(Destinations.KASIR_PROFIL) }
            )
        }

        composable(Destinations.KASIR_SCAN) {
            ScanKeranjangScreen(
                viewModel = kasirViewModel,
                onBack = { navController.popBackStack() },
                onNavigateToPembayaran = { navController.navigate(Destinations.KASIR_PEMBAYARAN) }
            )
        }

        composable(Destinations.KASIR_PEMBAYARAN) {
            PembayaranScreen(
                viewModel = kasirViewModel,
                onBack = { navController.popBackStack() },
                onNavigateToStruk = {
                    navController.navigate(Destinations.KASIR_STRUK) {
                        popUpTo(Destinations.KASIR_BERANDA) { inclusive = false }
                    }
                }
            )
        }

        composable(Destinations.KASIR_STRUK) {
            StrukDigitalScreen(
                viewModel = kasirViewModel,
                onNavigateToHome = {
                    navController.navigate(Destinations.KASIR_BERANDA) {
                        popUpTo(Destinations.KASIR_BERANDA) { inclusive = true }
                    }
                }
            )
        }

        composable(Destinations.KASIR_BARANG_MASUK) {
            BarangMasukScreen(
                viewModel = kasirViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Destinations.KASIR_STOK_OPNAME) {
            StokOpnameScreen(
                viewModel = kasirViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Destinations.KASIR_ABSENSI) {
            AbsensiScreen(
                viewModel = kasirViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Destinations.KASIR_DAFTAR_PRODUK) {
            DaftarProdukScreen(
                viewModel = kasirViewModel,
                onBack = { navController.popBackStack() },
                onNavigateToTambahProduk = { navController.navigate(Destinations.KASIR_PRODUK_BARU) }
            )
        }

        composable(Destinations.KASIR_PRODUK_BARU) {
            ProdukBaruScreen(
                viewModel = kasirViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Destinations.KASIR_RIWAYAT) {
            RiwayatTransaksiScreen(
                viewModel = kasirViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Destinations.KASIR_NOTIFIKASI) {
            KasirNotifikasiScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Destinations.KASIR_PROFIL) {
            KasirProfilPengaturanScreen(
                viewModel = kasirViewModel,
                onBack = { navController.popBackStack() },
                onLogout = {
                    navController.navigate(Destinations.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // --- OWNER SCREENS ---
        composable(Destinations.OWNER_DASHBOARD) {
            DashboardOwnerScreen(
                viewModel = ownerViewModel,
                onNavigateToKeuntungan = { navController.navigate(Destinations.OWNER_KEUNTUNGAN) },
                onNavigateToAbsensi = { navController.navigate(Destinations.OWNER_ABSENSI) },
                onNavigateToScanTidakBerhasil = { navController.navigate(Destinations.OWNER_SCAN_BERMASALAH) },
                onNavigateToSelisihStok = { navController.navigate(Destinations.OWNER_SELISIH_STOK) },
                onNavigateToEditHarga = { navController.navigate(Destinations.OWNER_EDIT_HARGA) },
                onNavigateToLogPersetujuan = { navController.navigate(Destinations.OWNER_LOG_PERSETUJUAN) },
                onNavigateToNotifikasi = { navController.navigate(Destinations.OWNER_NOTIFIKASI) },
                onNavigateToProfil = { navController.navigate(Destinations.OWNER_PROFIL) }
            )
        }

        composable(Destinations.OWNER_KEUNTUNGAN) {
            KeuntunganDetailScreen(
                viewModel = ownerViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Destinations.OWNER_ABSENSI) {
            MonitoringAbsensiScreen(
                viewModel = ownerViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Destinations.OWNER_SCAN_BERMASALAH) {
            ScanTidakBerhasilScreen(
                viewModel = ownerViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Destinations.OWNER_SELISIH_STOK) {
            SelisihStokScreen(
                viewModel = ownerViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Destinations.OWNER_EDIT_HARGA) {
            EditHargaDusScreen(
                viewModel = ownerViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Destinations.OWNER_LOG_PERSETUJUAN) {
            LogPersetujuanScreen(
                viewModel = ownerViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Destinations.OWNER_NOTIFIKASI) {
            OwnerNotifikasiScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Destinations.OWNER_PROFIL) {
            OwnerProfilPengaturanScreen(
                viewModel = ownerViewModel,
                onBack = { navController.popBackStack() },
                onLogout = {
                    navController.navigate(Destinations.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}
