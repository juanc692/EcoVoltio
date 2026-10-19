package com.atlas.ecoVoltio.data.model

data class DeviceShare(
    val name: String,
    val type: DeviceType,
    val percent: Int
)