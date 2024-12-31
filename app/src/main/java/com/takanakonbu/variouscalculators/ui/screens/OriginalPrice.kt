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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OriginalPriceCalculator() {
    var originalPrice by remember { mutableStateOf("") }
    var discountedPrice by remember { mutableStateOf("") }
    var discountRate by remember { mutableStateOf(0f) }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var showResult by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 元の価格入力フィールド
        OutlinedTextField(
            value = originalPrice,
            onValueChange = {
                showError = false
                showResult = false
                originalPrice = it.filter { char -> char.isDigit() }
            },
            label = { Text("元の価格") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            isError = showError && errorMessage.contains("元の価格"),
            supportingText = if (showError && errorMessage.contains("元の価格")) {
                { Text(errorMessage) }
            } else null
        )

        // 割引後価格入力フィールド
        OutlinedTextField(
            value = discountedPrice,
            onValueChange = {
                showError = false
                showResult = false
                discountedPrice = it.filter { char -> char.isDigit() }
            },
            label = { Text("割引後価格") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            isError = showError && errorMessage.contains("割引後"),
            supportingText = if (showError && errorMessage.contains("割引後")) {
                { Text(errorMessage) }
            } else null
        )

        // 計算ボタン
        Button(
            onClick = {
                when {
                    originalPrice.isEmpty() -> {
                        showError = true
                        errorMessage = "元の価格を入力してください"
                        showResult = false
                    }
                    discountedPrice.isEmpty() -> {
                        showError = true
                        errorMessage = "割引後価格を入力してください"
                        showResult = false
                    }
                    originalPrice.toLongOrNull() ?: 0 <= 0 -> {
                        showError = true
                        errorMessage = "元の価格は0より大きい値を入力してください"
                        showResult = false
                    }
                    discountedPrice.toLongOrNull() ?: 0 <= 0 -> {
                        showError = true
                        errorMessage = "割引後価格は0より大きい値を入力してください"
                        showResult = false
                    }
                    discountedPrice.toLong() >= originalPrice.toLong() -> {
                        showError = true
                        errorMessage = "割引後価格は元の価格より小さい値を入力してください"
                        showResult = false
                    }
                    else -> {
                        val original = originalPrice.toDouble()
                        val discounted = discountedPrice.toDouble()

                        // 割引率を計算（小数点第一位まで）
                        discountRate = ((original - discounted) / original * 100).roundToInt() / 1f
                        showError = false
                        errorMessage = ""
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
                        text = "割引率: $discountRate%",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "割引額: ${(originalPrice.toLong() - discountedPrice.toLong()).toString().chunked(3).joinToString(",")}円",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}