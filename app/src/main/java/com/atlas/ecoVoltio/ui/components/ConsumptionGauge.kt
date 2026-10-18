package com.atlas.ecoVoltio.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.atlas.ecoVoltio.ui.theme.*

@Composable
fun ConsumptionGauge(
    value: Double,
    max: Double,
    modifier: Modifier = Modifier
) {
    val progress = (value / max).toFloat().coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(800),
        label = "gaugeProgress"
    )

    val (color, statusText) = when {
        progress >= 1f -> Overlimit to "Límite superado"
        progress >= 0.8f -> Energy to "Cerca del límite"
        else -> Savings to "Dentro del rango"
    }

    Box(modifier = modifier.size(200.dp), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 16.dp.toPx()
            val inset = strokeWidth / 2
            val arcSize = Size(size.width - strokeWidth, size.height - strokeWidth)
            val style = Stroke(width = strokeWidth, cap = StrokeCap.Round)

            // Riel de fondo: arco de 270° que empieza abajo a la izquierda
            drawArc(
                color = GaugeTrack,
                startAngle = 135f,
                sweepAngle = 270f,
                useCenter = false,
                topLeft = Offset(inset, inset),
                size = arcSize,
                style = style
            )
            // Progreso
            drawArc(
                color = color,
                startAngle = 135f,
                sweepAngle = 270f * animatedProgress,
                useCenter = false,
                topLeft = Offset(inset, inset),
                size = arcSize,
                style = style
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "%.1f".format(value),
                color = TextPrimary,
                fontSize = 38.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(text = "de ${max.toInt()} kWh hoy", color = TextSecondary, fontSize = 13.sp)
            Spacer(Modifier.height(8.dp))
            Text(text = statusText, color = color, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF14181F)
@Composable
private fun ConsumptionGaugePreview() {
    EcoVoltioTheme {
        ConsumptionGauge(value = 14.6, max = 18.0)
    }
}