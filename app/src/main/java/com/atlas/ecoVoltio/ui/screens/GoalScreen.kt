package com.atlas.ecoVoltio.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.atlas.ecoVoltio.data.MockData
import com.atlas.ecoVoltio.ui.components.GoalCard
import com.atlas.ecoVoltio.ui.theme.*

@Composable
fun GoalsScreen(modifier: Modifier = Modifier) {
    val goals = MockData.goals

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 22.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("Metas de ahorro", color = TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "${goals.size} metas activas",
                color = TextSecondary,
                fontSize = 13.sp,
                modifier = Modifier.weight(1f)
            )
            NewGoalButton(onClick = { /* pendiente: formulario de nueva meta */ })
        }

        goals.forEach { GoalCard(it) }
    }
}

@Composable
private fun NewGoalButton(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(9.dp))
            .background(Energy)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Filled.Add, contentDescription = null, tint = Background, modifier = Modifier.size(14.dp))
        Spacer(Modifier.width(6.dp))
        Text("Nueva meta", color = Background, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF14181F, heightDp = 700)
@Composable
private fun MetasScreenPreview() {
    EcoVoltioTheme {
        GoalsScreen()
    }
}

