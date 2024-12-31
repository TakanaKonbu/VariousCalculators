package com.takanakonbu.variouscalculators.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import java.text.NumberFormat
import java.util.*
import kotlin.math.roundToInt

@Composable
fun ProfitCalculation() {
    var revenue by remember { mutableStateOf("") }        // 売上
    var cost by remember { mutableStateOf("") }          // 原価
    var profitAmount by remember { mutableLongStateOf(0L) }    // 利益額
    var profitMargin by remember { mutableFloatStateOf(0f) }   // 利益率
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var showResult by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 売上入力フィールド
        OutlinedTextField(
            value = revenue,
            onValueChange = {
                if (it.isEmpty() || it.matches(Regex("^\\d*$"))) {
                    revenue = it
                    showError = false
                    showResult = false
                }
            },
            label = { Text("売上") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            isError = showError && errorMessage.contains("売上"),
            supportingText = if (showError && errorMessage.contains("売上")) {
                { Text(errorMessage) }
            } else { { Text("売上金額を入力してください") } }
        )

        // 原価入力フィールド
        OutlinedTextField(
            value = cost,
            onValueChange = {
                if (it.isEmpty() || it.matches(Regex("^\\d*$"))) {
                    cost = it
                    showError = false
                    showResult = false
                }
            },
            label = { Text("原価") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            isError = showError && errorMessage.contains("原価"),
            supportingText = if (showError && errorMessage.contains("原価")) {
                { Text(errorMessage) }
            } else { { Text("原価（仕入れ価格など）を入力してください") } }
        )

        // 計算ボタン
        Button(
            onClick = {
                when {
                    revenue.isEmpty() -> {
                        showError = true
                        errorMessage = "売上を入力してください"
                    }
                    cost.isEmpty() -> {
                        showError = true
                        errorMessage = "原価を入力してください"
                    }
                    revenue.toLongOrNull() == null -> {
                        showError = true
                        errorMessage = "売上は有効な数値を入力してください"
                    }
                    cost.toLongOrNull() == null -> {
                        showError = true
                        errorMessage = "原価は有効な数値を入力してください"
                    }
                    revenue.toLong() <= 0 -> {
                        showError = true
                        errorMessage = "売上は0より大きい値を入力してください"
                    }
                    cost.toLong() <= 0 -> {
                        showError = true
                        errorMessage = "原価は0より大きい値を入力してください"
                    }
                    cost.toLong() > revenue.toLong() -> {
                        showError = true
                        errorMessage = "原価は売上以下の値を入力してください"
                    }
                    else -> {
                        val revenueAmount = revenue.toLong()
                        val costAmount = cost.toLong()
                        profitAmount = revenueAmount - costAmount
                        profitMargin = ((profitAmount.toFloat() / revenueAmount.toFloat() * 100) * 100).roundToInt() / 100f
                        showError = false
                        showResult = true
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Text("計算する")
        }

        // 結果表示
        if (showResult) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "計算結果",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "売上: ${NumberFormat.getNumberInstance(Locale.JAPAN).format(revenue.toLong())}円",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "原価: ${NumberFormat.getNumberInstance(Locale.JAPAN).format(cost.toLong())}円",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "利益: ${NumberFormat.getNumberInstance(Locale.JAPAN).format(profitAmount)}円",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "利益率: $profitMargin%",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "計算式:\n利益 = 売上 - 原価\n利益率 = (利益 ÷ 売上) × 100",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    )
                }
            }
        }

        // 説明カード
        Card(
            modifier = Modifier.fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "用語説明",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "売上：商品やサービスの販売による収入\n" +
                            "原価：商品の仕入れ価格や製造にかかる費用\n" +
                            "利益：売上から原価を引いた金額\n" +
                            "利益率：売上に対する利益の割合（%）",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}