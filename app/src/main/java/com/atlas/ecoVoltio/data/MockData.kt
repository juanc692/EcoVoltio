package com.atlas.ecoVoltio.data

import com.atlas.ecoVoltio.data.model.DeviceType
import com.atlas.ecoVoltio.data.model.Goal
import com.atlas.ecoVoltio.data.model.PlugStatus
import com.atlas.ecoVoltio.data.model.SmartPlug

object MockData{
    const val DAILY_LIMIT_KWH = 18.0
    const val TODAY_KWH = 14.6

    var smartPlug = listOf(
        SmartPlug("p1","Aire acondicionado", "Habitacion principal", DeviceType.AIR_CONDITIONER,
            PlugStatus.ONLINE,1180,"Hace 4 seg"),
        SmartPlug("p2","Refrigerador","Cocina", DeviceType.FRIDGE, PlugStatus.ONLINE,210,
            "Hace 4 seg"),
        SmartPlug("p3","Lavadora","Patio", DeviceType.WASHER, PlugStatus.OFFLINE,null,
            "hace 2 h")
    )
    var goals = listOf(
        Goal(1, "Reducir 15% vs. mes pasado",62,"vas bien"),
        Goal(2,"Cero excesos de limite diario",40,"2 dias superados esta semana")
    )

    var weekUsage = listOf(
        "Lun" to 12.1, "Mar" to 15.8, "Mie" to 11.4, "Jue" to 17.9,
        "Vie" to 16.2, "Sab" to 19.5, "Dom" to 14.6
    )
}