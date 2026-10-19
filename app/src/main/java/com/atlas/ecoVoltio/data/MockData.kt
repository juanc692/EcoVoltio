package com.atlas.ecoVoltio.data

import com.atlas.ecoVoltio.data.model.DeviceShare
import com.atlas.ecoVoltio.data.model.DeviceType
import com.atlas.ecoVoltio.data.model.DiscoveredPlug
import com.atlas.ecoVoltio.data.model.Goal
import com.atlas.ecoVoltio.data.model.PlugStatus
import com.atlas.ecoVoltio.data.model.SmartPlug
import com.atlas.ecoVoltio.data.model.Tip

object MockData{
    const val DAILY_LIMIT_KWH = 18.0
    const val TODAY_KWH = 14.6

    const val ESTIMATED_BILL = 69200
    const val BILL_CHANGE_PERCENT = -6   // negativo = bajó respecto al mes pasado

    var smartPlug = listOf(
        SmartPlug("p1","Aire acondicionado", "Habitacion principal", DeviceType.AIR_CONDITIONER,
            PlugStatus.ONLINE,1180,"Hace 4 seg"),
        SmartPlug("p2","Refrigerador","Cocina", DeviceType.FRIDGE, PlugStatus.ONLINE,210,
            "Hace 4 seg"),
        SmartPlug("p3","Lavadora","Patio", DeviceType.WASHER, PlugStatus.OFFLINE,null,
            "hace 2 h")
    )

    val scanResults = listOf(
        DiscoveredPlug("n1", "Taco de luz nuevo"),
        DiscoveredPlug("n2", "Taco de luz nuevo"),
    )

    var weekUsage = listOf(
        "Lun" to 12.1, "Mar" to 15.8, "Mie" to 11.4, "Jue" to 17.9,
        "Vie" to 16.2, "Sab" to 19.5, "Dom" to 14.6
    )

    val monthUsage = listOf(
        "Sem 1" to 98.0, "Sem 2" to 112.0, "Sem 3" to 87.0, "Sem 4" to 74.0
    )

    // 24 valores: kWh consumidos en cada hora del día (índice = hora)
    val hourlyUsage = listOf(
        0.2, 0.1, 0.1, 0.1, 0.2, 0.2, 0.4, 0.5, 0.5, 0.5, 0.6, 0.7,
        0.7, 0.6, 0.6, 0.6, 0.7, 0.8, 1.0, 1.5, 1.7, 1.4, 0.7, 0.3
    )

    val deviceShares = listOf(
        DeviceShare("Aire acondicionado", DeviceType.AIR_CONDITIONER, 34),
        DeviceShare("Refrigerador", DeviceType.FRIDGE, 22),
        DeviceShare("Calentador de agua", DeviceType.WATER_HEATER, 18),
        DeviceShare("Lavadora", DeviceType.WASHER, 14),
        DeviceShare("TV y electrónicos", DeviceType.TV, 12),
    )

    val goals = listOf(
        Goal(1,"Reducir 15% vs. mes pasado", 62, "Vas bien: llevas 9% de reducción"),
        Goal(2,"No superar \$85.000 en la factura", 81, "Estimado actual: \$69.200"),
        Goal(3,"Cero excesos de límite diario", 40, "2 días superados esta semana"),
    )

    val tips = listOf(
        Tip(
            "Tu pico está entre 7 y 9 p.m.",
            "El aire acondicionado y el calentador coinciden en ese horario. Escalonar su uso 30 minutos podría bajar el pico un 15%.",
            "≈ \$9.400 / mes"
        ),
        Tip(
            "Refrigerador con consumo elevado",
            "Su consumo subió 8% esta semana. Revisa que las empaquetaduras cierren bien y que no esté junto a una fuente de calor.",
            "≈ \$4.100 / mes"
        ),
        Tip(
            "Modo eco en el aire acondicionado",
            "Subir 2 °C la temperatura promedio en las horas de la tarde reduce el consumo sin afectar mucho el confort.",
            "≈ \$12.600 / mes"
        ),
    )
}