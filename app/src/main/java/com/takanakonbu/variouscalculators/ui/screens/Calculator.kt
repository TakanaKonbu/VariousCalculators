package com.takanakonbu.variouscalculators.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
    var storedValue by remember { mutableStateOf(0.0) }
    var pendingOperator by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        // Display
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.3f)
                .background(MaterialTheme.colorScheme.surface),
            contentAlignment = Alignment.BottomEnd
        ) {
            Text(
                text = displayValue,
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
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
                    backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                ) {
                    displayValue = "0"
                    waitingForOperand = true
                    storedValue = 0.0
                    pendingOperator = null
                }
                CalculatorButton(
                    text = "±",
                    modifier = Modifier.weight(1f),
                    backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                ) {
                    displayValue = if (displayValue.startsWith("-")) {
                        displayValue.substring(1)
                    } else {
                        "-$displayValue"
                    }
                }
                CalculatorButton(
                    text = "%",
                    modifier = Modifier.weight(1f),
                    backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                ) {
                    val currentValue = displayValue.toDoubleOrNull() ?: 0.0
                    displayValue = (currentValue / 100).toString()
                }
                CalculatorButton(
                    text = "÷",
                    modifier = Modifier.weight(1f),
                    backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ) {
                    pendingOperator = "÷"
                    storedValue = displayValue.toDoubleOrNull() ?: 0.0
                    waitingForOperand = true
                }
            }

            // Number buttons rows
            val numberRows = listOf(
                listOf("7", "8", "9"),
                listOf("4", "5", "6"),
                listOf("1", "2", "3"),
                listOf("0", ".", "=")
            )

            numberRows.forEachIndexed { rowIndex, row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
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
                                        val result = when (pendingOperator) {
                                            "+" -> storedValue + currentValue
                                            "-" -> storedValue - currentValue
                                            "×" -> storedValue * currentValue
                                            "÷" -> storedValue / currentValue
                                            else -> currentValue
                                        }
                                        displayValue = result.toString()
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
        modifier = modifier
            .aspectRatio(1f)
            .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = contentColor
        ),
        shape = RoundedCornerShape(12.dp),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

