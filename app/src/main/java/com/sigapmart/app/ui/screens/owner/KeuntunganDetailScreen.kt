package com.sigapmart.app.ui.screens.owner

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sigapmart.app.ui.components.SigapHeaderBar

@Composable
fun KeuntunganDetailScreen(
    viewModel: OwnerViewModel,
    onBack: () -> Unit
) {
    var selectedTab by remember { mutableStateOf("Harian") }

    Scaffold(
        topBar = {
            SigapHeaderBar(
                title = "Keuntungan",
                onBack = onBack
            )
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
            // Tabs Bar (Harian, Mingguan, Bulanan)
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEFEBE4)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                ) {
                    listOf("Harian", "Mingguan", "Bulanan").forEach { tab ->
                        val isSelected = selectedTab == tab
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) Color.White else Color.Transparent)
                                .clickable { selectedTab = tab },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = tab,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 14.sp,
                                color = if (isSelected) Color(0xFFB81D24) else Color(0xFF64748B)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Keuntungan hari ini & Bar Chart Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Keuntungan hari ini", fontSize = 13.sp, color = Color(0xFF64748B))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Rp812.300",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E7D32)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Bar Chart (Sen - Min)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        ChartBar("Sen", 0.5f, false)
                        ChartBar("Sel", 0.6f, false)
                        ChartBar("Rab", 0.45f, false)
                        ChartBar("Kam", 0.7f, false)
                        ChartBar("Jum", 0.55f, false)
                        ChartBar("Sab", 0.85f, false)
                        ChartBar("Min", 0.65f, true)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text("Produk terlaris", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF1E293B))
            Spacer(modifier = Modifier.height(10.dp))

            // Best Seller Items matching mockup
            BestSellerItem("Indomie Goreng", "142 terjual · penjualan Rp497.000", "Rp99.400")
            BestSellerItem("Aqua 600ml", "118 terjual · penjualan Rp472.000", "Rp70.800")
            BestSellerItem("Gula Pasir 1kg", "64 terjual · penjualan Rp1.120.000", "Rp64.000")
        }
    }
}

@Composable
fun ChartBar(day: String, heightRatio: Float, isSelected: Boolean) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier.fillMaxHeight()
    ) {
        Box(
            modifier = Modifier
                .width(22.dp)
                .fillMaxHeight(heightRatio)
                .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                .background(if (isSelected) Color(0xFFB81D24) else Color(0xFFEF9A9A))
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(day, fontSize = 11.sp, color = Color(0xFF64748B))
    }
}

@Composable
fun BestSellerItem(name: String, detail: String, profit: String) {
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
                Text(detail, fontSize = 12.sp, color = Color(0xFF64748B))
            }
            Text(profit, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF1E293B))
        }
    }
}
