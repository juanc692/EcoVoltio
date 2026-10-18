package com.atlas.ecoVoltio.data.model
import android.graphics.drawable.Icon
import androidx.compose.ui.graphics.Color

data class Device(
    val int: Int,
    val name: String,
    val pct: Int,
    val kvh: Float,
    val color: Color
)
