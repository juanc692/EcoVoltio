package com.atlas.ecoVoltio.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.atlas.ecoVoltio.data.model.DeviceType
import com.atlas.ecoVoltio.data.model.PlugStatus
import com.atlas.ecoVoltio.data.model.SmartPlug
import com.atlas.ecoVoltio.ui.theme.*

// El modelo dice QUÉ es; la UI decide CÓMO se ve.
private fun DeviceType.icon(): ImageVector = when (this) {
    DeviceType.AIR_CONDITIONER -> Icons.Filled.AcUnit
    DeviceType.FRIDGE -> Icons.Filled.Kitchen
    DeviceType.WATER_HEATER -> Icons.Filled.Whatshot
    DeviceType.WASHER -> Icons.Filled.LocalLaundryService
    DeviceType.TV -> Icons.Filled.Tv
}

private fun DeviceType.accent(): Color = when (this) {
    DeviceType.AIR_CONDITIONER -> Energy
    DeviceType.FRIDGE -> Savings
    DeviceType.WATER_HEATER -> Overlimit
    DeviceType.WASHER -> Color(0xFF7B93FF)
    DeviceType.TV -> Color(0xFFB98CE0)
}

@Composable
fun DeviceCard(
    plug: SmartPlug,
    modifier: Modifier = Modifier
) {
    val accent = plug.type.accent()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .appCard(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(accent.copy(alpha = 0.13f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = plug.type.icon(),
                contentDescription = null,
                tint = accent,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(plug.name, color = TextPrimary, fontSize = 13.5.sp, fontWeight = FontWeight.SemiBold)
            Text(plug.room, color = TextSecondary, fontSize = 12.sp)
        }

        Column(horizontalAlignment = Alignment.End) {
            if (plug.status == PlugStatus.ONLINE) {
                Text(
                    text = plug.watts?.let { "$it W" } ?: "En espera",
                    color = Savings,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.WifiOff,
                        contentDescription = null,
                        tint = Overlimit,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text("Sin conexión", color = Overlimit, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }
            Text(plug.lastSync, color = TextMuted, fontSize = 10.5.sp)
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF14181F)
@Composable
private fun DeviceCardPreview() {
    EcoVoltioTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            MockData.smartPlug.forEach { DeviceCard(it) }
        }
    }
}