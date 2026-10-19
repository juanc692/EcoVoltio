package com.atlas.ecoVoltio.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.atlas.ecoVoltio.data.MockData
import com.atlas.ecoVoltio.data.model.PlugStatus
import com.atlas.ecoVoltio.ui.components.ConsumptionGauge
import com.atlas.ecoVoltio.ui.components.accent
import com.atlas.ecoVoltio.ui.components.appCard
import com.atlas.ecoVoltio.ui.components.icon
import com.atlas.ecoVoltio.ui.theme.*
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.abs

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val plugs = MockData.smartPlug
    val onlineCount = plugs.count { it.status == PlugStatus.ONLINE }
    val nearLimit = MockData.TODAY_KWH / MockData.DAILY_LIMIT_KWH >= 0.75

    val weekAverage = MockData.weekUsage.map { it.second }.average()
    val peakDay = MockData.weekUsage.maxByOrNull { it.second }?.first.orEmpty()

    val billChange = MockData.BILL_CHANGE_PERCENT
    val billDown = billChange < 0

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 22.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Header()

        if (nearLimit) {
            LimitAlert(today = MockData.TODAY_KWH, limit = MockData.DAILY_LIMIT_KWH)
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ConsumptionGauge(value = MockData.TODAY_KWH, max = MockData.DAILY_LIMIT_KWH)
            LiveIndicator(online = onlineCount, total = plugs.size)
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard(
                label = "Factura estimada",
                value = formatMoney(MockData.ESTIMATED_BILL),
                trendText = "${abs(billChange)}% ${if (billDown) "menos" else "más"} que el mes pasado",
                trendColor = if (billDown) Savings else Energy,
                trendIcon = if (billDown) Icons.AutoMirrored.Filled.TrendingDown
                else Icons.AutoMirrored.Filled.TrendingUp,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                label = "Promedio semanal",
                value = "%.1f kWh".format(weekAverage),
                trendText = "$peakDay es tu día más alto",
                trendColor = Energy,
                trendIcon = Icons.AutoMirrored.Filled.TrendingUp,
                modifier = Modifier.weight(1f)
            )
        }

        TopConsumers()
    }
}

@Composable
private fun Header() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier.weight(1f)) {
            Text("Buenas tardes", color = TextSecondary, fontSize = 13.sp)
            Text("Casa Rodríguez", color = TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(11.dp))
                .background(Energy.copy(alpha = 0.09f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Filled.Bolt, contentDescription = null, tint = Energy, modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
private fun LimitAlert(today: Double, limit: Double) {
    val shape = RoundedCornerShape(14.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(Color(0xFF2A2114))
            .border(1.dp, Energy.copy(alpha = 0.27f), shape)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            Icons.Filled.Notifications,
            contentDescription = null,
            tint = Energy,
            modifier = Modifier.padding(top = 2.dp).size(18.dp)
        )
        Column {
            Text("Cerca del límite diario", color = Energy, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            Text(
                text = "Llevas ${"%.1f".format(today)} kWh de ${limit.toInt()} kWh permitidos hoy.",
                color = TextSecondary,
                fontSize = 13.sp
            )
        }
    }
}

@Composable
private fun LiveIndicator(online: Int, total: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            Icons.Filled.FiberManualRecord,
            contentDescription = null,
            tint = Savings,
            modifier = Modifier.size(10.dp)
        )
        Spacer(Modifier.width(6.dp))
        Text("En vivo · $online de $total tacos reportando", color = TextSecondary, fontSize = 12.sp)
    }
}

@Composable
private fun StatCard(
    label: String,
    value: String,
    trendText: String,
    trendColor: Color,
    trendIcon: ImageVector,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.appCard()) {
        Text(label, color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
        Spacer(Modifier.height(4.dp))
        Text(value, color = TextPrimary, fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.Top) {
            Icon(trendIcon, contentDescription = null, tint = trendColor, modifier = Modifier.padding(top = 1.dp).size(13.dp))
            Spacer(Modifier.width(4.dp))
            Text(trendText, color = trendColor, fontSize = 12.sp)
        }
    }
}

@Composable
private fun TopConsumers() {
    // Solo tacos con lectura real, de mayor a menor consumo
    val top = MockData.smartPlug
        .filter { it.status == PlugStatus.ONLINE && it.watts != null }
        .sortedByDescending { it.watts }
        .take(3)
    val maxWatts = (top.firstOrNull()?.watts ?: 1).coerceAtLeast(1)

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("Qué más consume", color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)

        top.forEach { plug ->
            val accent = plug.type.accent()
            val watts = plug.watts ?: 0
            val barShape = RoundedCornerShape(3.dp)

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(9.dp))
                        .background(accent.copy(alpha = 0.13f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(plug.type.icon(), contentDescription = null, tint = accent, modifier = Modifier.size(16.dp))
                }
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row {
                        Text(plug.name, color = TextPrimary, fontSize = 13.sp, modifier = Modifier.weight(1f))
                        Text("$watts W", color = TextSecondary, fontSize = 13.sp)
                    }
                    Spacer(Modifier.height(5.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(5.dp)
                            .clip(barShape)
                            .background(SurfaceBorder)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(watts / maxWatts.toFloat())
                                .fillMaxHeight()
                                .clip(barShape)
                                .background(accent)
                        )
                    }
                }
            }
        }
    }
}

private fun formatMoney(amount: Int): String =
    "\$" + NumberFormat.getIntegerInstance(Locale.forLanguageTag("es-CO")).format(amount)

@Preview(showBackground = true, backgroundColor = 0xFF14181F, heightDp = 900)
@Composable
private fun InicioScreenPreview() {
    EcoVoltioTheme {
        HomeScreen()
    }
}