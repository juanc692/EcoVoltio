package com.atlas.ecoVoltio.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.atlas.ecoVoltio.data.MockData
import com.atlas.ecoVoltio.ui.components.TipCard
import com.atlas.ecoVoltio.ui.theme.*

@Composable
fun AdviceScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 22.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Column {
            Text("Consejos", color = TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(
                "Recomendaciones basadas en tus patrones de consumo de las últimas semanas.",
                color = TextSecondary,
                fontSize = 13.sp,
                lineHeight = 19.sp
            )
        }

        MockData.tips.forEach { TipCard(it) }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF14181F, heightDp = 800)
@Composable
private fun ConsejosScreenPreview() {
    EcoVoltioTheme {
        AdviceScreen()
    }
}

