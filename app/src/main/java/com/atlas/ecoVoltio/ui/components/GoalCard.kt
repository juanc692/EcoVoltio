package com.atlas.ecoVoltio.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
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
import com.atlas.ecoVoltio.data.model.Goal
import com.atlas.ecoVoltio.ui.theme.*

@Composable
fun GoalCard(
    goal: Goal,
    modifier: Modifier = Modifier
) {
    val progress = goal.progress.coerceIn(0, 100) / 100f
    val barColor = if (goal.progress >= 80) Savings else Energy
    val barShape = RoundedCornerShape(4.dp)

    Column(modifier = modifier.fillMaxWidth().appCard()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = goal.title,
                color = TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )
            if (goal.progress >= 100) {
                Icon(Icons.Filled.Check, contentDescription = "Meta cumplida", tint = Savings, modifier = Modifier.size(16.dp))
            }
        }

        Spacer(Modifier.height(12.dp))

        // Barra hecha a mano: dos Box, el de adelante ocupa la fracción del progreso
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(7.dp)
                .clip(barShape)
                .background(SurfaceBorder)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .fillMaxHeight()
                    .clip(barShape)
                    .background(barColor)
            )
        }

        Spacer(Modifier.height(8.dp))
        Text(goal.details, color = TextSecondary, fontSize = 12.sp)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF14181F)
@Composable
private fun GoalCardPreview() {
    EcoVoltioTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            MockData.goals.forEach { GoalCard(it) }
        }
    }
}