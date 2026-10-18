package com.atlas.ecoVoltio.data.model
import android.graphics.drawable.Icon

data class SmartPlug(
    val id: String,
    val name: String,
    val room: String,
    val type: DeviceType,
    val status: PlugStatus,
    val watts: Int?,
    val lastSync: String
)

enum class PlugStatus {ONLINE,OFFLINE}
enum class DeviceType { AIR_CONDITIONER, FRIDGE, WATER_HEATER, WASHER, TV}
