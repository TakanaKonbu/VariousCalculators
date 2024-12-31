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

@Composable
fun ExcludedTaxCalculator() {
    var amount by remember { mutableStateOf("") }
    var taxRate by remember { mutableFloatStateOf(0.10f) } // デフォルト税率10%
    var taxExcludedAmount by remember { mutableLongStateOf(0L) }
    var taxAmount by remember { mutableLongStateOf(0L) }
    var showError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 金額入力フィールド
        OutlinedTextField(
            value = amount,
            onValueChange = {
                showError = false
                amount = it.filter { char -> char.isDigit() }
            },
            label = { Text("税込金額") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            isError = showError,
            supportingText = if (showError) {
                { Text("金額を入力してください") }
            } else null
        )

        // 税率選択
        Text(
            text = "税率",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            // 10%のラジオボタン
            RadioButton(
                selected = taxRate == 0.10f,
                onClick = { taxRate = 0.10f }
            )
            Text(
                text = "10%",
                modifier = Modifier
                    .padding(start = 8.dp)
                    .align(Alignment.CenterVertically)
            )

            Spacer(modifier = Modifier.width(24.dp))

            // 8%のラジオボタン
            RadioButton(
                selected = taxRate == 0.08f,
                onClick = { taxRate = 0.08f }
            )
            Text(
                text = "8%",
                modifier = Modifier
                    .padding(start = 8.dp)
                    .align(Alignment.CenterVertically)
            )
        }

        // 計算ボタン
        Button(
            onClick = {
                if (amount.isNotEmpty()) {
                    val includedAmount = amount.toLongOrNull() ?: 0L
                    // 税抜金額を計算（小数点以下を四捨五入）
                    taxExcludedAmount = (includedAmount / (1 + taxRate)).roundToInt().toLong()
                    // 消費税額は税込金額から税抜金額を引いて計算
                    taxAmount = includedAmount - taxExcludedAmount
                    showError = false
                } else {
                    showError = true
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Text("計算する")
        }

        // 結果表示
        if (taxExcludedAmount > 0) {
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
                        text = "税抜金額: ${NumberFormat.getNumberInstance(Locale.JAPAN).format(taxExcludedAmount)}円",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "消費税額: ${NumberFormat.getNumberInstance(Locale.JAPAN).format(taxAmount)}円",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}