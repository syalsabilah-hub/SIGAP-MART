package com.sigapmart.app.ui.screens.kasir

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sigapmart.app.ui.components.SigapHeaderBar

@Composable
fun ProdukBaruScreen(
    viewModel: KasirViewModel,
    onBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var barcode by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Sembako") }
    var price by remember { mutableStateOf("") }
    var costPrice by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var boxSize by remember { mutableStateOf("12") }

    Scaffold(
        topBar = {
            SigapHeaderBar(
                title = "Tambah Produk Baru",
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
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    val tfColors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = Color(0xFFB81D24)
                    )

                    OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nama Produk") }, modifier = Modifier.fillMaxWidth(), colors = tfColors, shape = RoundedCornerShape(10.dp))
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(value = barcode, onValueChange = { barcode = it }, label = { Text("Kode Barcode") }, modifier = Modifier.fillMaxWidth(), colors = tfColors, shape = RoundedCornerShape(10.dp))
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(value = category, onValueChange = { category = it }, label = { Text("Kategori") }, modifier = Modifier.fillMaxWidth(), colors = tfColors, shape = RoundedCornerShape(10.dp))
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("Harga Jual (Rp)") }, modifier = Modifier.fillMaxWidth(), colors = tfColors, shape = RoundedCornerShape(10.dp))
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(value = costPrice, onValueChange = { costPrice = it }, label = { Text("Harga Modal (Rp)") }, modifier = Modifier.fillMaxWidth(), colors = tfColors, shape = RoundedCornerShape(10.dp))
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(value = stock, onValueChange = { stock = it }, label = { Text("Jumlah Stok Awal") }, modifier = Modifier.fillMaxWidth(), colors = tfColors, shape = RoundedCornerShape(10.dp))
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(value = boxSize, onValueChange = { boxSize = it }, label = { Text("Isi Per Dus (Pcs)") }, modifier = Modifier.fillMaxWidth(), colors = tfColors, shape = RoundedCornerShape(10.dp))

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            viewModel.addProduct(
                                name = name,
                                barcode = barcode,
                                category = category,
                                price = price.toDoubleOrNull() ?: 0.0,
                                costPrice = costPrice.toDoubleOrNull() ?: 0.0,
                                stock = stock.toIntOrNull() ?: 0,
                                boxSize = boxSize.toIntOrNull() ?: 1
                            )
                            onBack()
                        },
                        enabled = name.isNotBlank() && barcode.isNotBlank() && price.isNotBlank(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC800)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Simpan Produk", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF1E293B))
                    }
                }
            }
        }
    }
}
