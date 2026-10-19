package com.atlas.ecoVoltio.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.atlas.ecoVoltio.data.MockData
import com.atlas.ecoVoltio.ui.components.accent
import com.atlas.ecoVoltio.ui.components.appCard
import com.atlas.ecoVoltio.ui.components.icon
import com.atlas.ecoVoltio.ui.theme.*
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.compose.cartesian.data.columnModel
import com.patrykandpatrick.vico.compose.cartesian.data.lineModel
import com.patrykandpatrick.vico.compose.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.Fill
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianValueFormatter

private enum class Period(val label: String, val chartTitle: String) {
    WEEK("Semana", "Consumo por día (kWh)"),
    MONTH("Mes", "Consumo por semana (kWh)")
}

@Composable
fun StatisticScreen(modifier: Modifier = Modifier) {
    var period by remember { mutableStateOf(Period.WEEK) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 22.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text("Estadísticas", color = TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)

        PeriodToggle(selected = period, onSelect = { period = it })
        UsageChartCard(period = period)
        HourlyChartCard()
        DeviceDistribution()
    }
}

@Composable
private fun PeriodToggle(selected: Period, onSelect: (Period) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(CardBackground)
            .padding(4.dp)
    ) {
        Period.entries.forEach { p ->
            val isSelected = p == selected
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(9.dp))
                    .background(if (isSelected) Energy else CardBackground)
                    .clickable { onSelect(p) }
                    .padding(vertical = 9.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = p.label,
                    color = if (isSelected) Background else TextSecondary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun UsageChartCard(period: Period) {
    val data = if (period == Period.WEEK) MockData.weekUsage else MockData.monthUsage
    val labels = data.map { it.first }
    val bottomFormatter = remember(labels) {
        CartesianValueFormatter { _, x, _ -> labels.getOrElse(x.toInt()) { x.toInt().toString()} }
    }
    val modelProducer = remember { CartesianChartModelProducer() }

    // Cada vez que cambia el período, se publican datos nuevos y la gráfica se anima
    LaunchedEffect(period) {
        modelProducer.runTransaction {
            columnModel { series(data.map { it.second }) }
        }
    }

    Column(modifier = Modifier.fillMaxWidth().appCard()) {
        Text(period.chartTitle, color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
        Spacer(Modifier.height(8.dp))
        CartesianChartHost(
            chart = rememberCartesianChart(
                rememberColumnCartesianLayer(
                    ColumnCartesianLayer.ColumnProvider.series(
                        rememberLineComponent(fill = Fill(Energy), thickness = 16.dp)
                    )
                ),
                startAxis = VerticalAxis.rememberStart(),
                bottomAxis = HorizontalAxis.rememberBottom(valueFormatter = bottomFormatter),
            ),
            modelProducer = modelProducer,
        )
    }
}

@Composable
private fun HourlyChartCard() {
    val modelProducer = remember { CartesianChartModelProducer() }
    val hourly = MockData.hourlyUsage
    val peakHour = hourly.indexOf(hourly.max())
    val hourFormatter = remember {
        CartesianValueFormatter { _, x, _ -> "%02d".format(x.toInt()) }
    }
    val hourPlacer = remember { HorizontalAxis.ItemPlacer.aligned(spacing = { 3 }) }

    LaunchedEffect(Unit) {
        modelProducer.runTransaction {
            lineModel { series(hourly) }
        }
    }

    Column(modifier = Modifier.fillMaxWidth().appCard()) {
        Text("Consumo por hora del día (kWh)", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
        Spacer(Modifier.height(8.dp))
        CartesianChartHost(
            chart = rememberCartesianChart(
                rememberLineCartesianLayer(
                    LineCartesianLayer.LineProvider.series(
                        LineCartesianLayer.Line(LineCartesianLayer.LineFill.single(Fill(Savings)))
                    )
                ),
                startAxis = VerticalAxis.rememberStart(),
                bottomAxis = HorizontalAxis.rememberBottom(
                    valueFormatter = hourFormatter,
                    itemPlacer = hourPlacer
                ),
            ),
            modelProducer = modelProducer,
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = "Tu pico es a las %02d:00, cuando coinciden varios equipos.".format(peakHour),
            color = TextSecondary,
            fontSize = 12.sp
        )
    }
}

@Composable
private fun DeviceDistribution() {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            "Distribución por electrodoméstico",
            color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.SemiBold
        )

        MockData.deviceShares.forEach { share ->
            val accent = share.type.accent()
            val barShape = RoundedCornerShape(3.dp)

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(9.dp))
                        .background(accent.copy(alpha = 0.13f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(share.type.icon(), contentDescription = null, tint = accent, modifier = Modifier.size(16.dp))
                }
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row {
                        Text(share.name, color = TextPrimary, fontSize = 13.sp, modifier = Modifier.weight(1f))
                        Text("${share.percent}%", color = TextSecondary, fontSize = 13.sp)
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
                                .fillMaxWidth(share.percent / 100f)
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

@Preview(showBackground = true, backgroundColor = 0xFF14181F, heightDp = 1100)
@Composable
private fun EstadisticasScreenPreview() {
    EcoVoltioTheme {
        StatisticScreen()
    }
}