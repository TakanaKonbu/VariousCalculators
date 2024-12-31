package com.takanakonbu.variouscalculators.ui.screens

import android.annotation.SuppressLint
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

enum class TimeInputMode {
    HOURS,      // 時間入力
    MINUTES,    // 分入力
    SECONDS     // 秒入力
}

@SuppressLint("DefaultLocale")
@Composable
fun TimeCalculation() {
    var inputMode by remember { mutableStateOf(TimeInputMode.HOURS) }
    var inputValue by remember { mutableStateOf("") }
    var hours by remember { mutableFloatStateOf(0f) }
    var minutes by remember { mutableFloatStateOf(0f) }
    var seconds by remember { mutableFloatStateOf(0f) }
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
        // モード選択
        Text(
            text = "単位選択",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 8.dp)
        )

        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = inputMode == TimeInputMode.HOURS,
                    onClick = {
                        inputMode = TimeInputMode.HOURS
                        showResult = false
                        inputValue = ""
                    }
                )
                Text("時間で入力")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = inputMode == TimeInputMode.MINUTES,
                    onClick = {
                        inputMode = TimeInputMode.MINUTES
                        showResult = false
                        inputValue = ""
                    }
                )
                Text("分で入力")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = inputMode == TimeInputMode.SECONDS,
                    onClick = {
                        inputMode = TimeInputMode.SECONDS
                        showResult = false
                        inputValue = ""
                    }
                )
                Text("秒で入力")
            }
        }

        // 入力フィールド
        OutlinedTextField(
            value = inputValue,
            onValueChange = {
                if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*$"))) {
                    inputValue = it
                    showError = false
                    showResult = false
                }
            },
            label = {
                Text(
                    when (inputMode) {
                        TimeInputMode.HOURS -> "時間"
                        TimeInputMode.MINUTES -> "分"
                        TimeInputMode.SECONDS -> "秒"
                    }
                )
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            isError = showError,
            supportingText = if (showError) {
                { Text(errorMessage) }
            } else {
                {
                    Text(
                        when (inputMode) {
                            TimeInputMode.HOURS -> "時間を入力してください"
                            TimeInputMode.MINUTES -> "分を入力してください"
                            TimeInputMode.SECONDS -> "秒を入力してください"
                        }
                    )
                }
            }
        )

        // 計算ボタン
        Button(
            onClick = {
                when {
                    inputValue.isEmpty() -> {
                        showError = true
                        errorMessage = "値を入力してください"
                    }
                    inputValue.toFloatOrNull() == null -> {
                        showError = true
                        errorMessage = "有効な数値を入力してください"
                    }
                    inputValue.toFloat() < 0 -> {
                        showError = true
                        errorMessage = "0以上の値を入力してください"
                    }
                    else -> {
                        val value = inputValue.toFloat()
                        when (inputMode) {
                            TimeInputMode.HOURS -> {
                                hours = value
                                minutes = value * 60
                                seconds = minutes * 60
                            }
                            TimeInputMode.MINUTES -> {
                                hours = value / 60
                                minutes = value
                                seconds = value * 60
                            }
                            TimeInputMode.SECONDS -> {
                                hours = value / 3600
                                minutes = value / 60
                                seconds = value
                            }
                        }
                        showError = false
                        showResult = true
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Text("変換する")
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
                        text = "変換結果",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    when (inputMode) {
                        TimeInputMode.HOURS -> {
                            Text(
                                text = "${inputValue}時間は",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = "${String.format("%.1f", minutes)}分",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = "${String.format("%.1f", seconds)}秒",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                        TimeInputMode.MINUTES -> {
                            Text(
                                text = "${inputValue}分は",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = "${String.format("%.2f", hours)}時間",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = "${String.format("%.1f", seconds)}秒",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                        TimeInputMode.SECONDS -> {
                            Text(
                                text = "${inputValue}秒は",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = "${String.format("%.2f", hours)}時間",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = "${String.format("%.1f", minutes)}分",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }

                    Text(
                        text = "計算式:\n" + when (inputMode) {
                            TimeInputMode.HOURS ->
                                "分 = 時間 × 60\n秒 = 分 × 60"
                            TimeInputMode.MINUTES ->
                                "時間 = 分 ÷ 60\n秒 = 分 × 60"
                            TimeInputMode.SECONDS ->
                                "時間 = 秒 ÷ 3600\n分 = 秒 ÷ 60"
                        },
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
                    text = "使い方",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "1. 入力する単位を選択\n" +
                            "2. 変換したい値を入力\n" +
                            "3. 「変換する」ボタンをタップ\n\n" +
                            "※小数点以下も入力可能です（例：1.5時間）",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}