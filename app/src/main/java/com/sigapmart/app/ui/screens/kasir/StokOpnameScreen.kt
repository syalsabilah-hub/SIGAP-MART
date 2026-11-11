package com.sigapmart.app.ui.screens.kasir

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sigapmart.app.ui.components.SigapHeaderBar

@Composable
fun StokOpnameScreen(
    viewModel: KasirViewModel,
    onBack: () -> Unit
) {
    val products by viewModel.allProducts.collectAsState()

    Scaffold(
        topBar = {
            SigapHeaderBar(
                title = "Stok Opname",
                subtitle = "Rak B",
                onBack = onBack
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Button(
                    onClick = { onBack() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB81D24)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = "Scan dan Hitung Barang",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFFAF7F2))
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("8 dari 12 barang dihitung", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF1E293B))
                Text("2 selisih", fontSize = 13.sp, color = Color(0xFF64748B))
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Item rows matching mockup
            OpnameItemCard("Indomie Goreng", "Sistem 40 · Fisik 38", "Selisih -2", isDiff = true)
            OpnameItemCard("Aqua 600ml", "Sistem 84 · Fisik 84", "Sesuai", isDiff = false)
            OpnameItemCard("Gula Pasir 1kg", "Sistem 15 · Fisik 18", "Selisih +3", isDiff = true)
            OpnameItemCard("Minyak Goreng 1L", "Sistem 4 · Fisik 4", "Sesuai", isDiff = false)
            OpnameItemCard("Teh Botol", "Sistem 30 · Fisik 30", "Sesuai", isDiff = false)
            OpnameItemCard("Kopi Sachet", "Sistem 60 · Fisik 60", "Sesuai", isDiff = false)
        }
    }
}

@Composable
fun OpnameItemCard(name: String, detail: String, statusText: String, isDiff: Boolean) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(name, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF1E293B))
                Spacer(modifier = Modifier.height(2.dp))
                Text(detail, fontSize = 13.sp, color = Color(0xFF64748B))
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isDiff) Color(0xFFFFF3E0) else Color(0xFFE8F5E9))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = statusText,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDiff) Color(0xFFE65100) else Color(0xFF2E7D32)
                )
            }
        }
    }
}
