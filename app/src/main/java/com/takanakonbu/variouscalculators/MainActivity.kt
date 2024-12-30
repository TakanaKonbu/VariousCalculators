package com.takanakonbu.variouscalculators

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.takanakonbu.variouscalculators.ui.screens.DiscountCalculator
import com.takanakonbu.variouscalculators.ui.screens.ExcludedTaxCalculator
import com.takanakonbu.variouscalculators.ui.screens.OriginalPriceCalculator
import com.takanakonbu.variouscalculators.ui.screens.TaxIncludedCalculator
import com.takanakonbu.variouscalculators.ui.theme.VariousCalculatorsTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import com.takanakonbu.variouscalculators.ui.screens.ConcentrationCalculation
import com.takanakonbu.variouscalculators.ui.screens.ProbabilityCalculation
import com.takanakonbu.variouscalculators.ui.screens.ProfitCalculation
import com.takanakonbu.variouscalculators.ui.screens.TimeCalculation


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

    Scaffold(
        topBar = {
            // メイン画面以外でTopAppBarを表示
            if (navController.currentBackStackEntryAsState().value?.destination?.route != "main") {
                TopAppBar(
                    title = { Text("TOPに戻る") },
                    navigationIcon = {
                        IconButton(onClick = { navController.navigate("main") {
                            // バックスタックをクリアしてメイン画面に戻る
                            popUpTo("main") { inclusive = true }
                        } }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back to TOP"
                            )
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "main",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("main") {
                MainScreen(navController)
            }
            composable("taxIncluded") {
                TaxIncludedCalculator()
            }
            composable("excludedTax") {
                ExcludedTaxCalculator()
            }
            composable("discount") {
                DiscountCalculator()
            }
            composable("originalPrice") {
                OriginalPriceCalculator()
            }
            composable("concentration") {
                ConcentrationCalculation()
            }
            composable("probability") {
                ProbabilityCalculation()
            }
            composable("profit") {
                ProfitCalculation()
            }
            composable("time") {
                TimeCalculation()
            }
        }
    }
}

@Composable
fun MainScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "計算ツール",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // メニューアイテムのリスト
        val menuItems = listOf(
            "税込み計算" to "taxIncluded",
            "税抜き計算" to "excludedTax",
            "割引計算" to "discount",
            "元の値段計算" to "originalPrice",
            "濃度計算" to "concentration",
            "確率計算" to "probability",
            "利益計算" to "profit",
            "時間計算" to "time"
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(menuItems) { (title, route) ->
                AnimatedMenuButton(title = title) {
                    navController.navigate(route)
                }
            }
        }
    }
}

@Composable
fun AnimatedMenuButton(title: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .clickable(onClick = onClick)
            .animateContentSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
