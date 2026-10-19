package com.atlas.ecoVoltio.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Power
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.atlas.ecoVoltio.ui.screens.AdviceScreen
import com.atlas.ecoVoltio.ui.screens.DevicesScreen
import com.atlas.ecoVoltio.ui.screens.GoalsScreen
import com.atlas.ecoVoltio.ui.screens.HomeScreen
import com.atlas.ecoVoltio.ui.screens.StatisticScreen
import com.atlas.ecoVoltio.ui.theme.*

// Una sola fuente de verdad para las pestañas: ruta, texto e ícono
enum class Destination(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    INICIO("inicio", "Inicio", Icons.Filled.Home),
    TACOS("tacos", "Tacos", Icons.Filled.Power),
    ESTADISTICAS("estadisticas", "Estadísticas", Icons.Filled.BarChart),
    CONSEJOS("consejos", "Consejos", Icons.Filled.Lightbulb),
    METAS("metas", "Metas", Icons.Filled.TrackChanges)
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    Scaffold(
        containerColor = Background,
        bottomBar = {
            NavigationBar(
                containerColor = CardBackground,
                tonalElevation = 0.dp
            ) {
                Destination.entries.forEach { destination ->
                    NavigationBarItem(
                        selected = currentRoute == destination.route,
                        onClick = {
                            navController.navigate(destination.route) {
                                // Evita apilar pantallas: siempre se vuelve al inicio del grafo
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(destination.icon, contentDescription = destination.label)
                        },
                        label = {
                            Text(destination.label, fontSize = 10.sp)
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Energy,
                            selectedTextColor = Energy,
                            unselectedIconColor = TextMuted,
                            unselectedTextColor = TextMuted,
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Destination.INICIO.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Destination.INICIO.route) { HomeScreen() }
            composable(Destination.TACOS.route) { DevicesScreen() }
            composable(Destination.ESTADISTICAS.route) { StatisticScreen() }
            composable(Destination.CONSEJOS.route) { AdviceScreen() }
            composable(Destination.METAS.route) { GoalsScreen() }
        }
    }
}

