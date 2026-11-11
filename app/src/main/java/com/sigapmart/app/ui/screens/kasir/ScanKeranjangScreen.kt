package com.sigapmart.app.ui.screens.kasir

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sigapmart.app.data.model.Product
import com.sigapmart.app.ui.components.SigapHeaderBar

@Composable
fun ScanKeranjangScreen(
    viewModel: KasirViewModel,
    onBack: () -> Unit,
    onNavigateToPembayaran: () -> Unit
) {
    val context = LocalContext.current
    val allProducts by viewModel.allProducts.collectAsState()
    val cartItems by viewModel.cartItems.collectAsState()
    val totalAmount by viewModel.totalCartAmount.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf("SCAN_POS") } // SCAN_POS or KERANJANG

    val filteredProducts = allProducts.filter {
        it.name.contains(searchQuery, ignoreCase = true) || it.barcode.contains(searchQuery)
    }

    Scaffold(
        topBar = {
            SigapHeaderBar(
                title = "Scan & Keranjang Kasir",
                onBack = onBack,
                actions = {
                    if (cartItems.isNotEmpty()) {
                        IconButton(onClick = { viewModel.clearCart() }) {
                            Icon(Icons.Default.Delete, contentDescription = "Kosongkan Keranjang", tint = Color.White)
                        }
                    }
                }
            )
        },
        bottomBar = {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(8.dp),
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Total (${cartItems.sumOf { it.quantity }} barang)", fontSize = 13.sp, color = Color(0xFF64748B))
                            Text(
                                text = "Rp" + String.format("%,.0f", totalAmount).replace(',', '.'),
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E293B)
                            )
                        }

                        Button(
                            onClick = onNavigateToPembayaran,
                            enabled = cartItems.isNotEmpty(),
                            modifier = Modifier
                                .height(48.dp)
                                .width(160.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB81D24)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Bayar (${cartItems.sumOf { it.quantity }})", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFFAF7F2))
        ) {
            // Scanner Viewfinder Area (Dark Navy)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF1A2332))
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 220.dp, height = 100.dp)
                        .border(2.dp, Color(0xFFFFC800), RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.QrCodeScanner, contentDescription = null, tint = Color(0xFFFFC800), modifier = Modifier.size(36.dp))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Kamera POS Aktif", fontSize = 12.sp, color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = {
                        if (allProducts.isNotEmpty()) {
                            val randomProd = allProducts.random()
                            viewModel.addToCart(randomProd)
                            Toast.makeText(context, "Berhasil scan: ${randomProd.name}", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC800)),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Icon(Icons.Default.QrCodeScanner, contentDescription = null, tint = Color(0xFF1E293B), modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Simulasi Scan Barcode", fontWeight = FontWeight.Bold, color = Color(0xFF1E293B), fontSize = 13.sp)
                }
            }

            // Search Bar & Tab Toggle
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Cari barang atau ketik barcode...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = Color(0xFFB81D24)
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Mode Tabs: POS Katalog vs Keranjang
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFEFEBE4))
                        .padding(3.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (selectedTab == "SCAN_POS") Color.White else Color.Transparent)
                            .clickable { selectedTab = "SCAN_POS" },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Katalog POS (${filteredProducts.size})", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = if (selectedTab == "SCAN_POS") Color(0xFFB81D24) else Color(0xFF64748B))
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (selectedTab == "KERANJANG") Color.White else Color.Transparent)
                            .clickable { selectedTab = "KERANJANG" },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Keranjang (${cartItems.sumOf { it.quantity }})", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = if (selectedTab == "KERANJANG") Color(0xFFB81D24) else Color(0xFF64748B))
                    }
                }
            }

            // Product List / Cart List
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (selectedTab == "SCAN_POS") {
                    items(filteredProducts) { prod ->
                        val inCart = cartItems.find { it.product.id == prod.id }
                        PosCatalogCard(
                            product = prod,
                            quantityInCart = inCart?.quantity ?: 0,
                            onAdd = { viewModel.addToCart(prod) },
                            onRemove = { viewModel.removeFromCart(prod) }
                        )
                    }
                } else {
                    if (cartItems.isEmpty()) {
                        item {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 24.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(24.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text("Keranjang Masih Kosong", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF1E293B))
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Scan barcode atau pilih barang dari katalog POS", fontSize = 13.sp, color = Color(0xFF64748B))
                                }
                            }
                        }
                    } else {
                        items(cartItems) { item ->
                            PosCartItemCard(
                                product = item.product,
                                quantity = item.quantity,
                                onAdd = { viewModel.addToCart(item.product) },
                                onRemove = { viewModel.removeFromCart(item.product) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PosCatalogCard(
    product: Product,
    quantityInCart: Int,
    onAdd: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
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
            Column(modifier = Modifier.weight(1f)) {
                Text(product.name, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF1E293B))
                Text("Stok: ${product.stockSystem} | Kode: ${product.barcode}", fontSize = 12.sp, color = Color(0xFF64748B))
                Text("Rp" + String.format("%,.0f", product.price).replace(',', '.'), fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF2E7D32))
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (quantityInCart > 0) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .border(1.dp, Color(0xFFCBD5E1), RoundedCornerShape(8.dp))
                            .clickable { onRemove() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = "Kurang", tint = Color(0xFFB81D24), modifier = Modifier.size(16.dp))
                    }

                    Text(
                        text = "$quantityInCart",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFB81D24))
                        .clickable { onAdd() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Tambah", tint = Color.White, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}

@Composable
fun PosCartItemCard(
    product: Product,
    quantity: Int,
    onAdd: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
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
            Column(modifier = Modifier.weight(1f)) {
                Text(product.name, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF1E293B))
                Spacer(modifier = Modifier.height(2.dp))
                Text("Rp" + String.format("%,.0f", product.price).replace(',', '.') + " × $quantity", fontSize = 13.sp, color = Color(0xFF64748B))
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, Color(0xFFCBD5E1), RoundedCornerShape(8.dp))
                        .clickable { onRemove() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Remove, contentDescription = "Kurang", tint = Color(0xFFB81D24), modifier = Modifier.size(16.dp))
                }

                Text(
                    text = "$quantity",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )

                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, Color(0xFFCBD5E1), RoundedCornerShape(8.dp))
                        .clickable { onAdd() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Tambah", tint = Color(0xFFB81D24), modifier = Modifier.size(16.dp))
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "Rp" + String.format("%,.0f", product.price * quantity).replace(',', '.'),
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color(0xFF1E293B)
                )
            }
        }
    }
}
