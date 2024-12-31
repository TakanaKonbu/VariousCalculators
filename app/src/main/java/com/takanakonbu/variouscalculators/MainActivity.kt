package com.takanakonbu.variouscalculators

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.PriceChange
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import com.takanakonbu.variouscalculators.ui.screens.Calculator
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
            composable("calculation") {
                Calculator()
            }

        }
    }
}

@Composable
fun MainScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surface,
                        MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "計算ツール",
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            val menuItems = listOf(
                MenuItem("税込み計算", "taxIncluded", Icons.Filled.Calculate),
                MenuItem("税抜き計算", "excludedTax", Icons.Filled.Numbers),
                MenuItem("割引計算", "discount", Icons.Filled.Percent),
                MenuItem("元の値段計算", "originalPrice", Icons.Filled.PriceChange),
                MenuItem("濃度計算", "concentration", Icons.Filled.Science),
                MenuItem("確率計算", "probability", Icons.AutoMirrored.Filled.ShowChart),
                MenuItem("利益計算", "profit", Icons.Filled.Payments),
                MenuItem("時間計算", "time", Icons.Filled.Timer),
                MenuItem("計算機", "calculation", Icons.Filled.Timer)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = 8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(menuItems) { item ->
                    AnimatedMenuCard(item = item) {
                        navController.navigate(item.route)
                    }
                }
            }
        }
    }
}

data class MenuItem(
    val title: String,
    val route: String,
    val icon: ImageVector
)

@Composable
fun AnimatedMenuCard(
    item: MenuItem,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .shadow(
                elevation = 4.dp,
                spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                shape = RoundedCornerShape(16.dp)
            )
            .animateContentSize(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .padding(bottom = 8.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
        }
    }
}