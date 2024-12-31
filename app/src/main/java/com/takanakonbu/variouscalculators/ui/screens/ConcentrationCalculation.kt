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
fun ConcentrationCalculation() {
    var calculationMode by remember { mutableStateOf(CalculationMode.FIND_CONCENTRATION) }
    var soluteAmount by remember { mutableStateOf("") }
    var solutionAmount by remember { mutableStateOf("") }
    var targetConcentration by remember { mutableStateOf("") }
    var resultConcentration by remember { mutableStateOf(0f) }
    var resultSoluteAmount by remember { mutableStateOf(0f) }
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
        // 計算モード選択
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "濃度計算モード選択",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        // モード切り替えタブ
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            TabRow(
                selectedTabIndex = calculationMode.ordinal,
                modifier = Modifier.fillMaxWidth()
            ) {
                Tab(
                    selected = calculationMode == CalculationMode.FIND_CONCENTRATION,
                    onClick = {
                        calculationMode = CalculationMode.FIND_CONCENTRATION
                        showResult = false
                    }
                ) {
                    Text(
                        "濃度を求める",
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }
                Tab(
                    selected = calculationMode == CalculationMode.FIND_SOLUTE,
                    onClick = {
                        calculationMode = CalculationMode.FIND_SOLUTE
                        showResult = false
                    }
                ) {
                    Text(
                        "溶質量を求める",
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }
            }
        }

        when (calculationMode) {
            CalculationMode.FIND_CONCENTRATION -> {
                // 溶質の量入力フィールド
                OutlinedTextField(
                    value = soluteAmount,
                    onValueChange = {
                        if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*$"))) {
                            soluteAmount = it
                            showError = false
                            showResult = false
                        }
                    },
                    label = { Text("溶質の質量 (g)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    isError = showError && errorMessage.contains("溶質"),
                    supportingText = if (showError && errorMessage.contains("溶質")) {
                        { Text(errorMessage) }
                    } else { { Text("溶かす物質の質量を入力してください") } }
                )

                // 溶液の量入力フィールド
                OutlinedTextField(
                    value = solutionAmount,
                    onValueChange = {
                        if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*$"))) {
                            solutionAmount = it
                            showError = false
                            showResult = false
                        }
                    },
                    label = { Text("溶液の質量 (g)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    isError = showError && errorMessage.contains("溶液"),
                    supportingText = if (showError && errorMessage.contains("溶液")) {
                        { Text(errorMessage) }
                    } else { { Text("溶液全体の質量を入力してください") } }
                )
            }
            CalculationMode.FIND_SOLUTE -> {
                // 目標濃度入力フィールド
                OutlinedTextField(
                    value = targetConcentration,
                    onValueChange = {
                        if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*$"))) {
                            targetConcentration = it
                            showError = false
                            showResult = false
                        }
                    },
                    label = { Text("目標濃度 (%)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    isError = showError && errorMessage.contains("濃度"),
                    supportingText = if (showError && errorMessage.contains("濃度")) {
                        { Text(errorMessage) }
                    } else { { Text("作りたい濃度を入力してください") } }
                )

                // 溶液の量入力フィールド
                OutlinedTextField(
                    value = solutionAmount,
                    onValueChange = {
                        if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*$"))) {
                            solutionAmount = it
                            showError = false
                            showResult = false
                        }
                    },
                    label = { Text("溶液の質量 (g)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    isError = showError && errorMessage.contains("溶液"),
                    supportingText = if (showError && errorMessage.contains("溶液")) {
                        { Text(errorMessage) }
                    } else { { Text("作りたい溶液の質量を入力してください") } }
                )
            }
        }

        // 計算ボタン
        Button(
            onClick = {
                when (calculationMode) {
                    CalculationMode.FIND_CONCENTRATION -> {
                        when {
                            soluteAmount.isEmpty() -> {
                                showError = true
                                errorMessage = "溶質の質量を入力してください"
                            }
                            solutionAmount.isEmpty() -> {
                                showError = true
                                errorMessage = "溶液の質量を入力してください"
                            }
                            soluteAmount.toFloatOrNull() == null || soluteAmount.toFloat() <= 0 -> {
                                showError = true
                                errorMessage = "溶質の質量は0より大きい値を入力してください"
                            }
                            solutionAmount.toFloatOrNull() == null || solutionAmount.toFloat() <= 0 -> {
                                showError = true
                                errorMessage = "溶液の質量は0より大きい値を入力してください"
                            }
                            soluteAmount.toFloat() > solutionAmount.toFloat() -> {
                                showError = true
                                errorMessage = "溶質の質量は溶液の質量より小さい値を入力してください"
                            }
                            else -> {
                                val solute = soluteAmount.toFloat()
                                val solution = solutionAmount.toFloat()
                                resultConcentration = ((solute / solution * 100) * 100).roundToInt() / 100f
                                showError = false
                                showResult = true
                            }
                        }
                    }
                    CalculationMode.FIND_SOLUTE -> {
                        when {
                            targetConcentration.isEmpty() -> {
                                showError = true
                                errorMessage = "目標濃度を入力してください"
                            }
                            solutionAmount.isEmpty() -> {
                                showError = true
                                errorMessage = "溶液の質量を入力してください"
                            }
                            targetConcentration.toFloatOrNull() == null || targetConcentration.toFloat() <= 0 -> {
                                showError = true
                                errorMessage = "濃度は0より大きい値を入力してください"
                            }
                            targetConcentration.toFloat() >= 100 -> {
                                showError = true
                                errorMessage = "濃度は100%未満を入力してください"
                            }
                            solutionAmount.toFloatOrNull() == null || solutionAmount.toFloat() <= 0 -> {
                                showError = true
                                errorMessage = "溶液の質量は0より大きい値を入力してください"
                            }
                            else -> {
                                val concentration = targetConcentration.toFloat()
                                val solution = solutionAmount.toFloat()
                                // 溶質の質量 = (濃度 × 溶液の質量) ÷ 100
                                resultSoluteAmount = ((concentration * solution / 100) * 100).roundToInt() / 100f
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
                        CalculationMode.FIND_CONCENTRATION -> {
                            Text(
                                text = "質量パーセント濃度: $resultConcentration %",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "計算式: (溶質の質量 ÷ 溶液の質量) × 100",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                            )
                        }
                        CalculationMode.FIND_SOLUTE -> {
                            Text(
                                text = "必要な溶質の質量: $resultSoluteAmount g",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "計算式: (目標濃度 × 溶液の質量) ÷ 100",
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
            modifier = Modifier.fillMaxWidth(),
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
                        CalculationMode.FIND_CONCENTRATION ->
                            "溶質：溶かされる物質\n溶液：溶質が溶媒に溶けた混合物全体\n質量パーセント濃度：溶液全体に対する溶質の質量の割合"
                        CalculationMode.FIND_SOLUTE ->
                            "目標濃度：作りたい溶液の濃度（%）\n溶液の質量：作りたい溶液の全体量\n必要な溶質量：目標の濃度を達成するために必要な溶質の質量"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}

enum class CalculationMode {
    FIND_CONCENTRATION,  // 濃度を求めるモード
    FIND_SOLUTE         // 溶質量を求めるモード
}