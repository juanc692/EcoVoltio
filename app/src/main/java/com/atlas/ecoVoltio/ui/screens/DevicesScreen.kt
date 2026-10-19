package com.atlas.ecoVoltio.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Power
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.atlas.ecoVoltio.data.MockData
import com.atlas.ecoVoltio.data.model.DeviceType
import com.atlas.ecoVoltio.data.model.DiscoveredPlug
import com.atlas.ecoVoltio.data.model.PlugStatus
import com.atlas.ecoVoltio.data.model.SmartPlug
import com.atlas.ecoVoltio.ui.components.DeviceCard
import com.atlas.ecoVoltio.ui.components.appCard
import com.atlas.ecoVoltio.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun DevicesScreen(modifier: Modifier = Modifier) {
    val plugs = remember { mutableStateListOf(*MockData.smartPlug.toTypedArray()) }
    val found = remember { mutableStateListOf<DiscoveredPlug>() }
    var scanning by remember { mutableStateOf(false) }

    // Simula el escaneo: espera un momento y "encuentra" los tacos que aún no están vinculados
    LaunchedEffect(scanning) {
        if (scanning) {
            delay(1800)
            val linked = plugs.map { it.id }.toSet()
            found.clear()
            found.addAll(MockData.scanResults.filter { it.id !in linked })
            scanning = false
        }
    }

    val onlineCount = plugs.count { it.status == PlugStatus.ONLINE }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 22.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column {
            Text("Mis tacos", color = TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("Dispositivos que miden tu consumo", color = TextSecondary, fontSize = 13.sp)
        }

        SummaryCard(online = onlineCount, total = plugs.size)

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            plugs.forEach { DeviceCard(it) }
        }

        if (found.isNotEmpty()) {
            FoundSection(
                found = found,
                onConnect = { item ->
                    plugs.add(
                        SmartPlug(
                            id = item.id,
                            name = item.name,
                            room = "Sin asignar",
                            type = DeviceType.OTHER,
                            status = PlugStatus.ONLINE,
                            watts = 0,
                            lastSync = "ahora"
                        )
                    )
                    found.remove(item)
                }
            )
        }

        PairButton(scanning = scanning, onClick = { scanning = true })
    }
}

@Composable
private fun SummaryCard(online: Int, total: Int) {
    Row(
        modifier = Modifier.fillMaxWidth().appCard(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(11.dp))
                .background(Savings.copy(alpha = 0.13f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Filled.Power, contentDescription = null, tint = Savings, modifier = Modifier.size(20.dp))
        }
        Spacer(Modifier.width(14.dp))
        Column {
            Text(
                "$online de $total tacos conectados",
                color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold
            )
            Text(
                "Los datos en vivo mejoran la precisión de tus estadísticas",
                color = TextSecondary, fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun FoundSection(
    found: SnapshotStateList<DiscoveredPlug>,
    onConnect: (DiscoveredPlug) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("Encontrados cerca", color = TextSecondary, fontSize = 12.sp)

        found.forEach { item ->
            Row(
                modifier = Modifier.fillMaxWidth().appCard(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(TextSecondary.copy(alpha = 0.13f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Power, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(18.dp))
                }
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(item.name, color = TextPrimary, fontSize = 13.5.sp, fontWeight = FontWeight.SemiBold)
                    Text("Sin asignar", color = TextSecondary, fontSize = 12.sp)
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Energy)
                        .clickable { onConnect(item) }
                        .padding(horizontal = 12.dp, vertical = 7.dp)
                ) {
                    Text("Conectar", color = Background, fontSize = 12.5.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
private fun PairButton(scanning: Boolean, onClick: () -> Unit) {
    val borderColor = SurfaceBorder
    val contentColor = if (scanning) TextMuted else TextPrimary

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(enabled = !scanning, onClick = onClick)
            .drawBehind {
                // Compose no trae borde punteado: se dibuja a mano
                drawRoundRect(
                    color = borderColor,
                    cornerRadius = CornerRadius(12.dp.toPx()),
                    style = Stroke(
                        width = 1.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 8f))
                    )
                )
            }
            .padding(vertical = 14.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (scanning) {
            CircularProgressIndicator(
                modifier = Modifier.size(15.dp),
                strokeWidth = 2.dp,
                color = TextMuted
            )
        } else {
            Icon(Icons.Filled.Search, contentDescription = null, tint = contentColor, modifier = Modifier.size(16.dp))
        }
        Spacer(Modifier.width(8.dp))
        Text(
            text = if (scanning) "Buscando tacos cercanos…" else "Vincular nuevo taco de luz",
            color = contentColor, fontSize = 13.5.sp, fontWeight = FontWeight.SemiBold
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF14181F, heightDp = 800)
@Composable
private fun DispositivosScreen() {
    EcoVoltioTheme {
        DevicesScreen()
    }
}