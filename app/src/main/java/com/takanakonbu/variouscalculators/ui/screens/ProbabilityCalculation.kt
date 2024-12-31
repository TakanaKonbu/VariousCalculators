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
import kotlin.math.pow
import kotlin.math.roundToInt

enum class ProbabilityMode {
    BASIC,      // 基本確率計算
    MULTI       // 複数回試行の確率計算
}

@Composable
fun ProbabilityCalculation() {
    var calculationMode by remember { mutableStateOf(ProbabilityMode.BASIC) }

    // 基本確率計算用の状態
    var success by remember { mutableStateOf("") }
    var total by remember { mutableStateOf("") }
    var probability by remember { mutableFloatStateOf(0f) }

    // 複数回試行の確率計算用の状態
    var targetProbability by remember { mutableStateOf("") }
    var trials by remember { mutableStateOf("") }
    var successProbability by remember { mutableFloatStateOf(0f) }

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
        // タブ切り替え
        TabRow(
            selectedTabIndex = calculationMode.ordinal,
            modifier = Modifier.fillMaxWidth()
        ) {
            Tab(
                selected = calculationMode == ProbabilityMode.BASIC,
                onClick = {
                    calculationMode = ProbabilityMode.BASIC
                    showResult = false
                }
            ) {
                Text(
                    "基本確率",
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
            Tab(
                selected = calculationMode == ProbabilityMode.MULTI,
                onClick = {
                    calculationMode = ProbabilityMode.MULTI
                    showResult = false
                }
            ) {
                Text(
                    "複数回試行",
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (calculationMode) {
            ProbabilityMode.BASIC -> {
                // 成功数入力フィールド
                OutlinedTextField(
                    value = success,
                    onValueChange = {
                        if (it.isEmpty() || it.matches(Regex("^\\d*$"))) {
                            success = it
                            showError = false
                            showResult = false
                        }
                    },
                    label = { Text("成功数") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    isError = showError && errorMessage.contains("成功数"),
                    supportingText = if (showError && errorMessage.contains("成功数")) {
                        { Text(errorMessage) }
                    } else { { Text("成功した回数を入力してください") } }
                )

                // 総数入力フィールド
                OutlinedTextField(
                    value = total,
                    onValueChange = {
                        if (it.isEmpty() || it.matches(Regex("^\\d*$"))) {
                            total = it
                            showError = false
                            showResult = false
                        }
                    },
                    label = { Text("総数") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    isError = showError && errorMessage.contains("総数"),
                    supportingText = if (showError && errorMessage.contains("総数")) {
                        { Text(errorMessage) }
                    } else { { Text("試行した総回数を入力してください") } }
                )
            }
            ProbabilityMode.MULTI -> {
                // 確率入力フィールド
                OutlinedTextField(
                    value = targetProbability,
                    onValueChange = {
                        if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*$"))) {
                            targetProbability = it
                            showError = false
                            showResult = false
                        }
                    },
                    label = { Text("確率 (%)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    isError = showError && errorMessage.contains("確率"),
                    supportingText = if (showError && errorMessage.contains("確率")) {
                        { Text(errorMessage) }
                    } else { { Text("1回あたりの当たる確率を入力してください") } }
                )

                // 試行回数入力フィールド
                OutlinedTextField(
                    value = trials,
                    onValueChange = {
                        if (it.isEmpty() || it.matches(Regex("^\\d*$"))) {
                            trials = it
                            showError = false
                            showResult = false
                        }
                    },
                    label = { Text("試行回数") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    isError = showError && errorMessage.contains("試行"),
                    supportingText = if (showError && errorMessage.contains("試行")) {
                        { Text(errorMessage) }
                    } else { { Text("試行する回数を入力してください") } }
                )
            }
        }

        // 計算ボタン
        Button(
            onClick = {
                when (calculationMode) {
                    ProbabilityMode.BASIC -> {
                        when {
                            success.isEmpty() -> {
                                showError = true
                                errorMessage = "成功数を入力してください"
                            }
                            total.isEmpty() -> {
                                showError = true
                                errorMessage = "総数を入力してください"
                            }
                            success.toIntOrNull() == null -> {
                                showError = true
                                errorMessage = "成功数は有効な数値を入力してください"
                            }
                            total.toIntOrNull() == null -> {
                                showError = true
                                errorMessage = "総数は有効な数値を入力してください"
                            }
                            success.toInt() > total.toInt() -> {
                                showError = true
                                errorMessage = "成功数は総数より小さい値を入力してください"
                            }
                            total.toInt() <= 0 -> {
                                showError = true
                                errorMessage = "総数は0より大きい値を入力してください"
                            }
                            else -> {
                                val successCount = success.toFloat()
                                val totalCount = total.toFloat()
                                probability = ((successCount / totalCount * 100) * 100).roundToInt() / 100f
                                showError = false
                                showResult = true
                            }
                        }
                    }
                    ProbabilityMode.MULTI -> {
                        when {
                            targetProbability.isEmpty() -> {
                                showError = true
                                errorMessage = "確率を入力してください"
                            }
                            trials.isEmpty() -> {
                                showError = true
                                errorMessage = "試行回数を入力してください"
                            }
                            targetProbability.toFloatOrNull() == null -> {
                                showError = true
                                errorMessage = "確率は有効な数値を入力してください"
                            }
                            targetProbability.toFloat() <= 0 || targetProbability.toFloat() >= 100 -> {
                                showError = true
                                errorMessage = "確率は0より大きく100未満の値を入力してください"
                            }
                            trials.toIntOrNull() == null || trials.toInt() <= 0 -> {
                                showError = true
                                errorMessage = "試行回数は0より大きい値を入力してください"
                            }
                            else -> {
                                val prob = targetProbability.toFloat() / 100
                                val trialCount = trials.toInt()
                                // 1回以上当たる確率の計算
                                val failureProbability =
                                    (1 - prob).toDouble().pow(trialCount.toDouble())
                                successProbability = ((1 - failureProbability) * 10000).roundToInt() / 100f
                                showError = false
                                showResult = true
                            }
                        }
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

                    when (calculationMode) {
                        ProbabilityMode.BASIC -> {
                            Text(
                                text = "確率: $probability%",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = "成功数: ${NumberFormat.getNumberInstance(Locale.JAPAN).format(success.toInt())}回",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = "総数: ${NumberFormat.getNumberInstance(Locale.JAPAN).format(total.toInt())}回",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = "計算式: (成功数 ÷ 総数) × 100",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                            )
                        }
                        ProbabilityMode.MULTI -> {
                            Text(
                                text = "1回以上当たる確率: ${successProbability}%",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = "1回あたりの確率: $targetProbability%",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = "試行回数: ${NumberFormat.getNumberInstance(Locale.JAPAN).format(trials.toInt())}回",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = "計算式: 1 - (1 - 確率)^試行回数",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                            )
                        }
                    }
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
                    text = when (calculationMode) {
                        ProbabilityMode.BASIC ->
                            "成功数：目的とする結果が得られた回数\n" +
                                    "総数：実験や試行を行った全体の回数\n" +
                                    "確率：ある事象が起こる可能性を百分率で表したもの"
                        ProbabilityMode.MULTI ->
                            "確率：1回の試行で当たる確率（%）\n" +
                                    "試行回数：くじを引く回数\n" +
                                    "1回以上当たる確率：指定回数試行して1回以上当たる確率"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}