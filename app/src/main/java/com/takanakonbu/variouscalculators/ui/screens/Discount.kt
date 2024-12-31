package com.takanakonbu.variouscalculators.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscountCalculator() {
    var originalPrice by remember { mutableStateOf("") }
    var discountRate by remember { mutableStateOf("") }
    var discountedPrice by remember { mutableStateOf(0L) }
    var discountAmount by remember { mutableStateOf(0L) }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 元値入力フィールド
        OutlinedTextField(
            value = originalPrice,
            onValueChange = {
                showError = false
                originalPrice = it.filter { char -> char.isDigit() }
            },
            label = { Text("元の金額") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            isError = showError && errorMessage == "金額を入力してください",
            supportingText = if (showError && errorMessage == "金額を入力してください") {
                { Text(errorMessage) }
            } else null
        )

        // 割引率入力フィールド
        OutlinedTextField(
            value = discountRate,
            onValueChange = {
                showError = false
                discountRate = it.filter { char -> char.isDigit() }
            },
            label = { Text("割引率（%）") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            isError = showError && errorMessage == "割引率を入力してください",
            supportingText = if (showError && errorMessage == "割引率を入力してください") {
                { Text(errorMessage) }
            } else null
        )

        // 計算ボタン
        Button(
            onClick = {
                when {
                    originalPrice.isEmpty() -> {
                        showError = true
                        errorMessage = "金額を入力してください"
                    }
                    discountRate.isEmpty() -> {
                        showError = true
                        errorMessage = "割引率を入力してください"
                    }
                    discountRate.toIntOrNull() ?: 0 > 100 -> {
                        showError = true
                        errorMessage = "割引率は100%以下にしてください"
                    }
                    else -> {
                        val price = originalPrice.toLong()
                        val rate = discountRate.toInt()

                        // 割引額を計算（小数点以下を四捨五入）
                        discountAmount = ((price * rate) / 100.0).roundToInt().toLong()
                        // 割引後価格を計算
                        discountedPrice = price - discountAmount

                        showError = false
                        errorMessage = ""
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
        if (discountedPrice > 0) {
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
                        text = "割引後金額: ${NumberFormat.getNumberInstance(Locale.JAPAN).format(discountedPrice)}円",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "割引額: ${NumberFormat.getNumberInstance(Locale.JAPAN).format(discountAmount)}円",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "割引率: $discountRate%",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}