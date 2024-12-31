package com.takanakonbu.variouscalculators

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.takanakonbu.variouscalculators.ui.screens.*
import com.takanakonbu.variouscalculators.ui.theme.VariousCalculatorsTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VariousCalculatorsTheme {
                MainApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val menuItems = listOf(
        MenuItem("計算機", "calculation", Icons.Filled.Calculate),
        MenuItem("税込み計算", "taxIncluded", Icons.Filled.Money),
        MenuItem("税抜き計算", "excludedTax", Icons.Filled.Numbers),
        MenuItem("割引計算", "discount", Icons.Filled.Percent),
        MenuItem("元の値段計算", "originalPrice", Icons.Filled.PriceChange),
        MenuItem("濃度計算", "concentration", Icons.Filled.Science),
        MenuItem("確率計算", "probability", Icons.AutoMirrored.Filled.ShowChart),
        MenuItem("利益計算", "profit", Icons.Filled.Payments),
        MenuItem("時間計算", "time", Icons.Filled.Timer)
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(12.dp))
                menuItems.forEach { item ->
                    NavigationDrawerItem(
                        icon = { Icon(item.icon, contentDescription = null) },
                        label = { Text(item.title) },
                        selected = false,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                            scope.launch {
                                drawerState.close()
                            }
                        }
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("計算ツール") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            }
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = "calculation",
                modifier = Modifier.padding(paddingValues)
            ) {
                composable("calculation") { Calculator() }
                composable("taxIncluded") { TaxIncludedCalculator() }
                composable("excludedTax") { ExcludedTaxCalculator() }
                composable("discount") { DiscountCalculator() }
                composable("originalPrice") { OriginalPriceCalculator() }
                composable("concentration") { ConcentrationCalculation() }
                composable("probability") { ProbabilityCalculation() }
                composable("profit") { ProfitCalculation() }
                composable("time") { TimeCalculation() }
            }
        }
    }
}

data class MenuItem(
    val title: String,
    val route: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)