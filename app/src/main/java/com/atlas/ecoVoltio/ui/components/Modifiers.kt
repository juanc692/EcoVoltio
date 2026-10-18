package com.atlas.ecoVoltio.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.atlas.ecoVoltio.ui.theme.CardBackground
import com.atlas.ecoVoltio.ui.theme.SurfaceBorder

private val CardShape = RoundedCornerShape(14.dp)

fun Modifier.appCard(): Modifier = this
    .clip(CardShape)
    .background(CardBackground)
    .border(1.dp, SurfaceBorder, CardShape)
    .padding(16.dp)