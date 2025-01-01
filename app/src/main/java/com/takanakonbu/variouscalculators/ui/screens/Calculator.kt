package com.takanakonbu.variouscalculators.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Calculator() {
    var displayValue by remember { mutableStateOf("0") }
    var waitingForOperand by remember { mutableStateOf(true) }
    var storedValue by remember { mutableDoubleStateOf(0.0) }
    var pendingOperator by remember { mutableStateOf<String?>(null) }
    var lastOperation by remember { mutableStateOf("") }

    // 計算結果を整形する関数
    fun formatResult(number: Double): String {
        return if (number % 1.0 == 0.0) {
            number.toLong().toString()
        } else {
            String.format("%.8f", number).trimEnd('0').trimEnd('.')
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        // Display Area
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.3f)
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.End
        ) {
            // Last Operation Display
            Text(
                text = lastOperation,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            // Main Display
            Text(
                text = displayValue,
                style = MaterialTheme.typography.displayLarge.copy(fontSize = 48.sp),
                color = MaterialTheme.colorScheme.onSurface,
            )
        }

        // Keypad
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.7f)
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Function buttons row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CalculatorButton(
                    text = "C",
                    modifier = Modifier.weight(1f),
                    backgroundColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                ) {
                    displayValue = "0"
                    waitingForOperand = true
                    storedValue = 0.0
                    pendingOperator = null
                    lastOperation = ""
                }
                CalculatorButton(
                    text = "±",
                    modifier = Modifier.weight(1f),
                    backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                ) {
                    if (displayValue != "0") {
                        displayValue = if (displayValue.startsWith("-")) {
                            displayValue.substring(1)
                        } else {
                            "-$displayValue"
                        }
                    }
                }
                CalculatorButton(
                    text = "%",
                    modifier = Modifier.weight(1f),
                    backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                ) {
                    val currentValue = displayValue.toDoubleOrNull() ?: 0.0
                    displayValue = formatResult(currentValue / 100)
                }
                CalculatorButton(
                    text = "÷",
                    modifier = Modifier.weight(1f),
                    backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ) {
                    pendingOperator = "÷"
                    storedValue = displayValue.toDoubleOrNull() ?: 0.0
                    lastOperation = "$displayValue ÷"
                    waitingForOperand = true
                }
            }

            // Number buttons rows
            val numberRows = listOf(
                listOf("7", "8", "9"),
                listOf("4", "5", "6"),
                listOf("1", "2", "3"),
                listOf("0", "⌫", ".")
            )

            numberRows.forEachIndexed { rowIndex, row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Equal button in the last row
                    if (rowIndex == 3) {
                        CalculatorButton(
                            text = "=",
                            modifier = Modifier.weight(1f),
                            backgroundColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ) {
                            if (pendingOperator != null && !waitingForOperand) {
                                val currentValue = displayValue.toDoubleOrNull() ?: 0.0
                                lastOperation += " $displayValue ="
                                val result = when (pendingOperator) {
                                    "+" -> storedValue + currentValue
                                    "-" -> storedValue - currentValue
                                    "×" -> storedValue * currentValue
                                    "÷" -> if (currentValue != 0.0) storedValue / currentValue else Double.POSITIVE_INFINITY
                                    else -> currentValue
                                }
                                displayValue = formatResult(result)
                                pendingOperator = null
                                waitingForOperand = true
                            }
                        }
                    }

                    row.forEach { digit ->
                        when (digit) {
                            "=" -> {
                                CalculatorButton(
                                    text = digit,
                                    modifier = Modifier.weight(1f),
                                    backgroundColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                ) {
                                    if (pendingOperator != null && !waitingForOperand) {
                                        val currentValue = displayValue.toDoubleOrNull() ?: 0.0
                                        lastOperation += " $displayValue ="
                                        val result = when (pendingOperator) {
                                            "+" -> storedValue + currentValue
                                            "-" -> storedValue - currentValue
                                            "×" -> storedValue * currentValue
                                            "÷" -> if (currentValue != 0.0) storedValue / currentValue else Double.POSITIVE_INFINITY
                                            else -> currentValue
                                        }
                                        displayValue = formatResult(result)
                                        pendingOperator = null
                                        waitingForOperand = true
                                    }
                                }
                            }
                            "." -> {
                                CalculatorButton(
                                    text = digit,
                                    modifier = Modifier.weight(1f),
                                    backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                                ) {
                                    if (!displayValue.contains(".")) {
                                        displayValue = if (waitingForOperand) "0." else "$displayValue."
                                        waitingForOperand = false
                                    }
                                }
                            }
                            "⌫" -> {
                                CalculatorButton(
                                    text = digit,
                                    modifier = Modifier.weight(1f),
                                    backgroundColor = MaterialTheme.colorScheme.errorContainer,
                                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                                ) {
                                    if (!waitingForOperand && displayValue.length > 1) {
                                        displayValue = displayValue.dropLast(1)
                                    } else {
                                        displayValue = "0"
                                        waitingForOperand = true
                                    }
                                }
                            }
                            else -> {
                                CalculatorButton(
                                    text = digit,
                                    modifier = Modifier.weight(1f),
                                    backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                                ) {
                                    if (waitingForOperand) {
                                        displayValue = digit
                                        waitingForOperand = false
                                    } else {
                                        displayValue = if (displayValue == "0") digit else displayValue + digit
                                    }
                                }
                            }
                        }
                    }
                    // Operators column
                    if (rowIndex < 3) {
                        val operator = when (rowIndex) {
                            0 -> "×"
                            1 -> "-"
                            2 -> "+"
                            else -> ""
                        }
                        CalculatorButton(
                            text = operator,
                            modifier = Modifier.weight(1f),
                            backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        ) {
                            pendingOperator = operator
                            storedValue = displayValue.toDoubleOrNull() ?: 0.0
                            lastOperation = "$displayValue $operator"
                            waitingForOperand = true
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CalculatorButton(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    contentColor: Color,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.aspectRatio(1f),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = contentColor
        ),
        shape = RoundedCornerShape(12.dp),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.headlineMedium,
            fontSize = 24.sp
        )
    }
}